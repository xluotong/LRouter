package com.billbook.lib.router.internel

import android.annotation.SuppressLint
import android.content.Intent
import android.content.pm.PackageManager
import com.billbook.lib.router.*

internal class RedirectInterceptor(private val routeContext: RouteContext) : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        val chain = chain as ChainInternal
        if (chain.request().mode == Request.Mode.ROUTE_ONLY) {
            return Response.Builder().code(Response.Code.REFUSE)
                .routeInfo(chain.route)
                .build()
        }
        val request = chain.request()
        if (request.mode == Request.Mode.REACHABLE && checkReachable(chain.route)) {
            return Response.Builder().code(Response.Code.OK)
                .build()
        }
        return chain.proceed(chain.request())
        val response = chain.proceed(request)
        if (request.mode == Request.Mode.START
            && request.redirectRequest != null
            && response.code != Response.Code.OK
        ) {
            return RouterInternal.newCall(
                request.redirectRequest!!.newBuilder().redirectRequest(null)
                    .build()
            ).execute()
        }
        return response
    }

    @SuppressLint("WrongConstant")
    private fun checkReachable(route: RouteInfo): Boolean {
        return when (route.type) {
            RouteType.ACTIVITY -> {
                val intent = Intent(routeContext.appContext, route.targetClass)
                return intent.resolveActivityInfo(
                    routeContext.appContext.packageManager,
                    PackageManager.MATCH_DEFAULT_ONLY
                ) != null
            }
            RouteType.SERVICE -> {
                routeContext.appContext.packageManager.resolveService(
                    Intent(routeContext.appContext, route.targetClass),
                    PackageManager.MATCH_DEFAULT_ONLY
                )
            }
            else -> null
        } != null
    }
}