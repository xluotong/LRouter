package com.billbook.lib.router.internel

import android.net.Uri
import com.billbook.lib.router.RouteInfo

/**
 * @author xluotong@gmail.com
 */
internal interface RouteCentral {
    fun register(routeInfo: RouteInfo)

    operator fun get(uri: Uri): RouteInfo

    operator fun get(targetClass: Class<*>): List<RouteInfo>
}

internal class DefaultRouteCentral : RouteCentral {
    private val routeTree: RouteTree = RouteTree()

    @Synchronized
    override fun register(routeInfo: RouteInfo) {
        routeTree.add(routeInfo)
    }

    @Synchronized
    override operator fun get(uri: Uri): RouteInfo = routeTree.findRoute(uri) ?: RouteInfo.EMPTY

    @Synchronized
    override fun get(targetClass: Class<*>): List<RouteInfo> = routeTree.findRoute(targetClass)
}