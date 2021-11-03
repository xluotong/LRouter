package com.billbook.lib.router

import com.billbook.lib.router.internal.RouteContext
import com.billbook.lib.router.internal.RouteContextImpl

object LRouter {

    private val context: RouteContext by lazy { RouteContextImpl() }

    fun <T> getService(clazz: Class<T>): T? {
        return context.serviceCentral.getService(clazz)
    }

    fun <T> getService(clazz: Class<T>, name: String): T? {
        return context.serviceCentral.getService(clazz, name)
    }

    fun <T> getService(clazz: Class<T>, vararg params: Any): T? {
        return context.serviceCentral.getService(clazz, params)
    }

    fun findRoute(clazz: Class<*>): RouteInfo? {
        TODO()
    }

    operator fun get(uri: String): RouteInfo? {
        TODO()
    }

    fun navigateTo(request: Request): Response {
        TODO()
    }
}
