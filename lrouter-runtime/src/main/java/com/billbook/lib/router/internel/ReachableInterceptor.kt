package com.billbook.lib.router.internel

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import com.billbook.lib.router.*

/**
 * @author xluotong@gmail.com
 */
class ReachableInterceptor(private val context: Context) : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        chain as ChainInternal
        val request = chain.request()
        val route = chain.route
        if (request.mode == Request.Mode.REACHABLE && checkReachable(route)) {
            return Response.Builder().code(Response.Code.OK)
                .build()
        }
        return chain.proceed(chain.request())
    }

    @SuppressLint("WrongConstant")
    private fun checkReachable(route: RouteInfo): Boolean {
        return when (route.type) {
            RouteType.ACTIVITY -> {
                val intent = Intent(context, route.targetClass)
                return intent.resolveActivityInfo(
                    context.packageManager,
                    PackageManager.MATCH_DEFAULT_ONLY
                ) != null
            }
            RouteType.SERVICE -> {
                context.packageManager.resolveService(
                    Intent(context, route.targetClass),
                    PackageManager.MATCH_DEFAULT_ONLY
                )
            }
            else -> null
        } != null
    }
}