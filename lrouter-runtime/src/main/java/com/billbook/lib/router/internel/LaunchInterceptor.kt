package com.billbook.lib.router.internel

import android.content.Intent
import androidx.fragment.app.Fragment
import com.billbook.lib.router.Interceptor
import com.billbook.lib.router.Request
import com.billbook.lib.router.Response
import com.billbook.lib.router.RouteType

/**
 * @author xluotong@gmail.com
 */
internal class LaunchInterceptor(private val client: RouteContext) : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        chain as ChainInternal
        val request = chain.request()
        val route = chain.route
        if (request.mode == Request.Mode.REACHABLE) {
            return Response.Builder().code(Response.Code.OK)
                .build()
        }
        val responseBuilder = Response.Builder()
        val call = chain.call() as RouteCallInternal
        when (route.type) {
            RouteType.ACTIVITY -> {
                call.withListener { onLaunchStart() }
                val intent = Intent(client.appContext, route.targetClass)
                request.flags?.let { intent.setFlags(it) }
                request.extras?.let { intent.putExtras(it) }
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                client.appContext.startActivity(intent)
                call.withListener { onLaunchEnd() }
            }
            RouteType.FRAGMENT -> {
                responseBuilder.fragment(
                    Fragment.instantiate(
                        client.appContext,
                        route.targetClass.canonicalName
                    )
                )
            }
        }
        return responseBuilder.code(Response.Code.OK).build()
    }
}