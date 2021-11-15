package com.billbook.lib.router.internel

import com.billbook.lib.router.*

/**
 * @author xluotong@gmail.com
 */
class PreparedInterceptor : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        chain as ChainInternal
        if (chain.route == RouteInfo.EMPTY) {
            return Response.Builder().code(Response.Code.NOF_FOUND)
                .routeInfo(chain.route)
                .build()
        }
        if (chain.route.path.startsWith(ROUTE_WILE_CHAR)) {
            return Response.Builder().code(Response.Code.REFUSE)
                .routeInfo(chain.route)
                .message("Route path can not startWith $ROUTE_WILE_CHAR")
                .build()
        }
        (chain.call() as RouteCallInternal).withListener { onRouteHit() }
        return chain.proceed(chain.request())
    }
}