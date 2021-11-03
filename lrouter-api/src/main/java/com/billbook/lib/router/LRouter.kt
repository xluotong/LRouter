package com.billbook.lib.router

import com.billbook.lib.router.internal.RouteContext
import com.billbook.lib.router.internal.RouteContextImpl

object LRouter {

    private val context: RouteContext by lazy { RouteContextImpl() }

    @JvmStatic
    fun <T> getService(clazz: Class<T>): T? {
        return context.serviceCentral.getService(clazz)
    }

    @JvmStatic
    fun <T> getService(clazz: Class<T>, name: String): T? {
        return context.serviceCentral.getService(clazz, name)
    }

    @JvmStatic
    fun <T> getService(clazz: Class<T>, vararg params: Any): T? {
        return context.serviceCentral.getService(clazz, params)
    }

    @JvmStatic
    fun findRoute(uri: String): RouteInfo? {
        TODO()
    }

    @JvmStatic
    fun navigateTo(request: Request): Response {
        TODO()
    }
}

inline operator fun LRouter.get(uri: String): RouteInfo? = findRoute(uri)

inline operator fun <T> LRouter.get(clazz: Class<T>): T? = getService(clazz)