package com.billbook.lib.router.internel

import com.billbook.lib.router.Interceptor
import com.billbook.lib.router.ROUTE_WILE_CHAR
import com.billbook.lib.router.Response
import com.billbook.lib.router.RouteInfo

/**
 * @author xluotong@gmail.com
 */
class PreparedInterceptor : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        chain as ChainInternal
        if (chain.route == RouteInfo.EMPTY) {
            return Response.Builder().code(Response.Code.NOF_FOUND)
                .build()
        }
        if (chain.route.path.startsWith(ROUTE_WILE_CHAR)) {
            return Response.Builder().code(Response.Code.REFUSE)
                .message("Route path can not startWith $ROUTE_WILE_CHAR")
                .build()
        }
        return chain.proceed(chain.request())
    }
}