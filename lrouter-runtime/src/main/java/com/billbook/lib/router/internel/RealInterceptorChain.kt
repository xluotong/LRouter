package com.billbook.lib.router.internel

import com.billbook.lib.router.Interceptor
import com.billbook.lib.router.Request
import com.billbook.lib.router.Response
import com.billbook.lib.router.RouteCall

/**
 * @author xluotong@gmail.com
 */
internal class RealInterceptorChain(
    private val call: RealCall,
    private val interceptors: List<Interceptor>,
    private val index: Int,
    private val request: Request,
) : Interceptor.Chain {

    internal fun copy(
        index: Int = this.index,
        request: Request = this.request
    ) = RealInterceptorChain(call, interceptors, index, request)

    override fun call(): RouteCall = call

    override fun request(): Request = request

    override fun proceed(request: Request): Response {
        check(index < interceptors.size)
        // Call the next interceptor in the chain.
        val next = copy(index = index + 1, request = request)
        val interceptor = interceptors[index]
        return interceptor.intercept(next)
    }
}