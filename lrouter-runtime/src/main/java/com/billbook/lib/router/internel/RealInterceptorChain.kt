package com.billbook.lib.router.internel

import com.billbook.lib.router.*

/**
 * @author xluotong@gmail.com
 */
internal class RealInterceptorChain(
    private val call: RouteCall,
    private val interceptors: List<Interceptor>,
    private val index: Int,
    private val request: Request,
    override val route: RouteInfo
) : ChainInternal {

    internal fun copy(
        index: Int = this.index,
        request: Request = this.request,
        interceptors: List<Interceptor> = this.interceptors
    ) = RealInterceptorChain(call, interceptors, index, request, route)

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