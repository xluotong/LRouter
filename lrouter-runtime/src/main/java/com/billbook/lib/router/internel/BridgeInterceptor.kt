package com.billbook.lib.router.internel

import com.billbook.lib.router.Interceptor
import com.billbook.lib.router.Response

/**
 * 创建拦截器对象
 * @author xluotong@gmail.com
 */
internal class BridgeInterceptor : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        chain as ChainInternal
        val request = chain.request()
        var extInterceptor = chain.route.interceptors?.mapNotNull {
            runCatching { it.cast(it.newInstance()) }.getOrNull()
        } ?: listOf()
        if (extInterceptor.isEmpty()) {
            val response = chain.proceed(request)
            return response.newBuilder().build()
        }
        extInterceptor += ContinueInterceptor(chain)
        val newChain = RealInterceptorChain(
            chain.call(),
            extInterceptor,
            0,
            request,
            chain.route
        )
        return newChain.proceed(request)
    }

    private class ContinueInterceptor(val continueChain: Interceptor.Chain) : Interceptor {

        override fun intercept(chain: Interceptor.Chain): Response {
            return continueChain.proceed(chain.request())
        }
    }
}