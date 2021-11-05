package com.billbook.lib.router.internel

import com.billbook.lib.router.Interceptor
import com.billbook.lib.router.Request
import com.billbook.lib.router.Response
import com.billbook.lib.router.RouteCall
import java.util.concurrent.atomic.AtomicBoolean

/**
 * @author xluotong@gmail.com
 */
internal class RealCall(
    private val client: RouteClient,
    private val originalRequest: Request
) : RouteCall {

    private val executed = AtomicBoolean()
    private val eventListener: EventListener = client.eventListenerFactory.create(this)

    override fun request(): Request = originalRequest

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
        val interceptors = mutableListOf<Interceptor>()
        interceptors += BridgeInterceptor(client.routeCentral)
        interceptors += LaunchInterceptor()
        val chain = RealInterceptorChain(this, interceptors, 0, originalRequest)
        return chain.proceed(originalRequest)
    }
}