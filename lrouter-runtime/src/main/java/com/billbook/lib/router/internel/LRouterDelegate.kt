package com.billbook.lib.router.internel

import com.billbook.lib.router.*
import com.billbook.lib.router.internal.ServiceProvider

/**
 * @author xluotong@gmail.com
 */
internal class LRouterDelegate : LRouter.Delegate {

    override fun <T> getService(clazz: Class<T>): T? {
        TODO("Not yet implemented")
    }

    override fun <T> getService(clazz: Class<T>, name: String): T? {
        TODO("Not yet implemented")
    }

    override fun <T> getService(clazz: Class<T>, vararg params: Any): T? {
        TODO("Not yet implemented")
    }

    override fun <T> getServiceProvider(clazz: Class<T>): ServiceProvider<T>? {
        TODO("Not yet implemented")
    }

    override fun newCall(request: Request): RouteCall {
        TODO("Not yet implemented")
    }

    override fun findRoute(uri: String): RouteInfo? {
        TODO("Not yet implemented")
    }

    override fun navigate(request: Request): Response {
        TODO("Not yet implemented")
    }
}