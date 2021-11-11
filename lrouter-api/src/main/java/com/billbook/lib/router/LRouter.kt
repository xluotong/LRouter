package com.billbook.lib.router

import android.net.Uri
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
    fun findRoute(url: String): RouteInfo? = delegate.findRoute(url)

    @JvmStatic
    fun findRoute(target: Any): List<RouteInfo> = delegate.findRoute(target)

    @JvmStatic
    fun newCall(request: Request): RouteCall = delegate.newCall(request)

    @JvmStatic
    infix fun navigateTo(request: Request): Response = delegate.navigateTo(request)

    @JvmStatic
    infix fun inject(any: Any) = delegate.inject(any)

    interface Delegate : RouteCall.Factory {
        fun <T> getService(clazz: Class<T>): T?
        fun <T> getService(clazz: Class<T>, name: String): T?
        fun <T> getService(clazz: Class<T>, vararg params: Any): T?
        fun <T> getServiceProvider(clazz: Class<T>): ServiceProvider<T>?
        fun inject(any: Any)
        fun findRoute(url: String): RouteInfo?
        fun findRoute(target: Any): List<RouteInfo>
        fun navigateTo(request: Request): Response
    }
}

//inline operator fun <T> LRouter.get(uriString: String): T? = findRoute(uri)

inline operator fun <T> LRouter.get(clazz: Class<T>): T? = getService(clazz)

inline fun String.toRouteRequest(): Request = Request.from(this)

inline fun Uri.toRouteRequest(): Request = Request.from(this)

inline fun LRouter.getFragment(uri: Uri): Fragment? {
    return navigateTo(Request.from(uri)).fragment
}

inline fun LRouter.getFragment(url: String): Fragment? = getFragment(url.toUri())