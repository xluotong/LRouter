package com.billbook.lib.router.internel

import android.net.Uri
import com.billbook.lib.router.RouteInfo

/**
 * @author xluotong@gmail.com
 */
internal interface RouteCentral {
    fun register(routeInfo: RouteInfo)

    operator fun get(uri: Uri): RouteInfo
}

internal class DefaultRouteCentral : RouteCentral {
    private val routeTree: RouteTree = RouteTree()

    override fun register(routeInfo: RouteInfo) {
        routeTree.add(routeInfo)
    }

    override operator fun get(uri: Uri): RouteInfo = routeTree.findRoute(uri) ?: RouteInfo.EMPTY
}