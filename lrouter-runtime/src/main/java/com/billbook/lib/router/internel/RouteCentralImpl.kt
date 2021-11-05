package com.billbook.lib.router.internel

import com.billbook.lib.router.RouteInfo

/**
 * @author xluotong@gmail.com
 */
internal interface RouteCentral {
    fun register(routeInfo: RouteInfo)

    fun findRoute(route: String): RouteInfo?

    companion object {
        val INSTANCE: RouteCentral = RouteCentralImpl()
    }
}

internal class RouteCentralImpl : RouteCentral {
    private val routeTree: RouteTree = RouteTree()

    override fun register(routeInfo: RouteInfo) {
        routeTree.add(routeInfo)
    }

    override fun findRoute(route: String): RouteInfo? = routeTree.search(route)
}