package com.billbook.lib.router.internel

import com.billbook.lib.router.*
import java.util.concurrent.atomic.AtomicBoolean

/**
 * @author xluotong@gmail.com
 */
internal interface RouteCallInternal : RouteCall {
    val eventListener: EventListener

    fun withListener(event: EventListener.() -> Unit) {
        eventListener.apply(event)
    }
}

internal class RealCall(
    private val routeContext: RouteContext,
    private val request: Request
) : RouteCallInternal {

    private val executed = AtomicBoolean()
    override val eventListener: EventListener = routeContext.eventListenerFactory.create(this)

    override fun request(): Request = request

    override fun execute(): Response {
        check(executed.compareAndSet(false, true)) { "Already Executed" }
        withListener { onStart() }
        try {
            return getResponseWithInterceptorChain()
        } finally {
            withListener { onFinished() }
        }
    }

    override fun isExecuted(): Boolean = executed.get()

    private fun getResponseWithInterceptorChain(): Response {
        val interceptors = mutableListOf<Interceptor>()
        interceptors += PreparedInterceptor()
        interceptors += BridgeInterceptor()
        interceptors += LaunchInterceptor(routeContext)
        val chain = RealInterceptorChain(
            this,
            interceptors,
            0,
            request,
            routeContext.routeCentral[request.uri]
        )
        return chain.proceed(request)
    }
}