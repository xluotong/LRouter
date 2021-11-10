package com.billbook.lib.router


/**
 * @author xluotong@gmail.com
 */
typealias RouteRequest = Request

typealias RouteResponse = Request

const val ROUTE_WILE_CHAR = "*"

data class RouteInfo(
    val path: String,
    val scheme: String,
    val host: String,
    val group: String,
    val groupDesc: String,
    val desc: String,
    val targetClass: Class<*>,
    val type: RouteType,
    val launcher: Class<out Launcher>,
    val interceptors: List<Class<out Interceptor>>?,
) {
    companion object {
        val EMPTY = RouteInfo(
            "", "", "", "", "", "", Any::class.java,
            RouteType.PROVIDER, Launcher::class.java, null
        )
    }
}

enum class RouteType {
    ACTIVITY,
    FRAGMENT,
    PROVIDER,
    SERVICE
}