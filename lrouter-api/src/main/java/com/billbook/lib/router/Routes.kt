package com.billbook.lib.router

import org.omg.PortableInterceptor.Interceptor

/**
 * @author xluotong@gmail.com
 */
data class RouteInfo(
    val name: String,
    val desc: String,
    val path: String,
    val scheme: String = "*",
    val host: String = "*",
    val targetClass:Class<*>,
    val interceptors: List<Class<out Interceptor>>,
    val type: RouteType,
)

enum class RouteType(name: String) {
    ACTIVITY("activity"),
    FRAGMENT("fragment"),
    PROVIDER("provider"),
    SERVICE("service")
}