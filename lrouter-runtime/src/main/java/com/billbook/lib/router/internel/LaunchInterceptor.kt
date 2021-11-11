package com.billbook.lib.router.internel

import android.content.Context
import androidx.fragment.app.Fragment
import com.billbook.lib.router.Interceptor
import com.billbook.lib.router.Launcher
import com.billbook.lib.router.Response
import com.billbook.lib.router.RouteType

/**
 * @author xluotong@gmail.com
 */
internal class LaunchInterceptor(private val routeContext: RouteContext) : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        chain as ChainInternal
        val request = chain.request()
        val route = chain.route
        val call = chain.call() as RouteCallInternal
        val fragment: Fragment? = request.fragment
        val context: Context = request.context ?: fragment?.activity ?: routeContext.appContext
        val responseBuilder = Response.Builder()
        when (route.type) {
            RouteType.ACTIVITY, RouteType.SERVICE -> {
                val launcher = if (chain.route.launcher::class == Launcher::class) {
                    DefaultIntentLauncher()
                } else {
                    runCatching { chain.route.launcher.newInstance() }.getOrNull() ?: DefaultIntentLauncher()
                }
                call.withListener { onLaunchStart() }
                try {
                    val contract = DefaultContract(request, route)
                    launcher.launch(context, launcher.createIntent(context, contract), contract)
                } finally {
                    call.withListener { onLaunchEnd() }
                }
            }
            RouteType.FRAGMENT -> {
                responseBuilder.fragment(
                    Fragment.instantiate(
                        context,
                        route.targetClass.canonicalName
                    )
                )
            }
        }
        return responseBuilder.code(Response.Code.OK).build()
    }
}