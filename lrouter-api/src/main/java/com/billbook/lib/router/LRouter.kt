package com.billbook.lib.router

import androidx.fragment.app.Fragment

object LRouter {

    private lateinit var delegate: Delegate

    fun initialize(delegate: Delegate) {
        this.delegate = delegate
    }

    @JvmStatic
    fun <T> getService(clazz: Class<T>): T? = delegate.getService(clazz)

    @JvmStatic
    fun <T> getService(clazz: Class<T>, name: String): T? = delegate.getService(clazz, name)

    @JvmStatic
    fun <T> getService(clazz: Class<T>, vararg params: Any): T? = delegate.getService(clazz, params)

    @JvmStatic
    fun <T> getServiceProvider(clazz: Class<T>): ServiceProvider<T>? {
        return delegate.getServiceProvider(clazz)
    }

    @JvmStatic
    fun newCall(request: Request): RouteCall = delegate.newCall(request)

    @JvmStatic
    infix fun navigate(request: Request): Response = delegate.navigate(request)

    interface Delegate {
        fun <T> getService(clazz: Class<T>): T?
        fun <T> getService(clazz: Class<T>, name: String): T?
        fun <T> getService(clazz: Class<T>, vararg params: Any): T?
        fun <T> getServiceProvider(clazz: Class<T>): ServiceProvider<T>?
        fun newCall(request: Request): RouteCall
        fun navigate(request: Request): Response
    }
}

inline operator fun LRouter.get(uri: String): RouteInfo? = findRoute(uri)

inline operator fun <T> LRouter.get(clazz: Class<T>): T? = getService(clazz)

inline fun String.toRouteRequest() = Request.from(this)

inline fun LRouter.getFragment(url: String): Fragment? {
    return navigate(url.toRouteRequest().newBuilder().build()).fragment
}

inline fun LRouter.findRoute(url: String): RouteInfo? {
    return navigate(
        url.toRouteRequest().newBuilder()
            .launchMode(Request.Mode.GET_ROUTE)
            .build()
    ).routeInfo
}