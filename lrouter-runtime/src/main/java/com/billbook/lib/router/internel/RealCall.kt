package com.billbook.lib.router.internel

import com.billbook.lib.router.Interceptor
import com.billbook.lib.router.Request
import com.billbook.lib.router.Response
import com.billbook.lib.router.RouteCall
import java.lang.IllegalStateException
import java.util.concurrent.atomic.AtomicBoolean

/**
 * @author xluotong@gmail.com
 */
internal class RealCall(
    private val client: RouteClient,
    private val request: Request
) : RouteCall {

    private val executed = AtomicBoolean()
    private val eventListener: EventListener = client.eventListenerFactory.create(this)

    override fun request(): Request = request

    override fun execute(): Response {
        check(executed.compareAndSet(false, true)) { "Already Executed" }
        eventListener.onStart()
        try {
            return getResponseWithInterceptorChain()
        } finally {
            eventListener.onFinished()
        }
    }

    override fun enqueue(callback: (Response) -> Unit): Response {
        TODO("Not yet implemented")
    }

    override fun cancel() {
        TODO("Not yet implemented")
    }

    override fun isExecuted(): Boolean = executed.get()

    private fun getResponseWithInterceptorChain(): Response {
        if (request.url.isEmpty()) throw IllegalStateException("route url is Empty!")
        val interceptors = mutableListOf<Interceptor>()
        interceptors += PreparedInterceptor()
        interceptors += BridgeInterceptor()
        interceptors += LaunchInterceptor(client)
        val chain = RealInterceptorChain(
            this,
            interceptors,
            0,
            request,
            client.routeCentral[request.url]
        )
        return chain.proceed(request)
    }
}