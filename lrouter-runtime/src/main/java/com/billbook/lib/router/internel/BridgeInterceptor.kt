package com.billbook.lib.router.internel

import com.billbook.lib.router.Interceptor
import com.billbook.lib.router.Response

/**
 * 创建拦截器对象
 * @author xluotong@gmail.com
 */
internal class BridgeInterceptor : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request()
        //
    }
}