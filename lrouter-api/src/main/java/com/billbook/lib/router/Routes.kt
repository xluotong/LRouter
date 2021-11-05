package com.billbook.lib.router


/**
 * @author xluotong@gmail.com
 */
typealias RouteRequest = Request

typealias RouteResponse = Request


data class RouteInfo(
    val path: String,
    val scheme: String,
    val host: String,
    val group: String,
    val groupDesc: String,
    val desc:String,
    val targetClass:Class<*>,
    val type: RouteType,
    val interceptors: List<Class<out Interceptor>>?,
)

enum class RouteType(name: String) {
    ACTIVITY("activity"),
    FRAGMENT("fragment"),
    PROVIDER("provider"),
    SERVICE("service")
}