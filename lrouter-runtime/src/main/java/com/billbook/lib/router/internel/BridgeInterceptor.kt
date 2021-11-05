package com.billbook.lib.router.internel

import com.billbook.lib.router.Interceptor
import com.billbook.lib.router.Response

/**
 * 创建拦截器对象
 * @author xluotong@gmail.com
 */
internal class BridgeInterceptor(private val routeCentral: RouteCentral) : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request()
        val routeInfo = routeCentral.findRoute(request.originalUrl)
            ?: return Response.Builder()
                .code(Response.Code.NOF_FOUND)
                .build()
        routeInfo.interceptors?.map {
            runCatching { it.cast(it.newInstance()) }.getOrNull()
        }?.filterNotNull()?.let {
            val chain = RealInterceptorChain(chain.call(),it,0, request)
            chain.proceed(request)
        }
        val response = chain.proceed(request)
        return response.newBuilder().routeInfo(routeInfo).build()
    }
}