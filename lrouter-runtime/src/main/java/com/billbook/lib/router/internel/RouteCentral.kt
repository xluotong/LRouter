package com.billbook.lib.router.internel

import com.billbook.lib.router.RouteInfo

/**
 * @author xluotong@gmail.com
 */
internal interface RouteCentral {
    fun register(routeInfo: RouteInfo)

    operator fun get(url: String): RouteInfo
}

internal class DefaultRouteCentral : RouteCentral {
    private val routeTree: RouteTree = RouteTree()

    override fun register(routeInfo: RouteInfo) {
        routeTree.add(routeInfo)
    }

    override operator fun get(url: String): RouteInfo = routeTree.findRoute(url)?:RouteInfo.EMPTY
}