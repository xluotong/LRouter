package com.billbook.lib.router.internel

import android.net.Uri
import com.billbook.lib.router.RouteInfo

internal const val EMPTY_STRING = ""

/**
 * @author xluotong@gmail.com
 */
internal class RouteTree {

    private val root: Node = Node(EMPTY_STRING, null, mutableMapOf(), false)

    fun add(routeInfo: RouteInfo) {
        val segments = mutableListOf(routeInfo.scheme, routeInfo.host) + routeInfo.path.split("/")
        var node = root
        segments.forEachIndexed { index, segment ->
            if (node.children == null) {
                node.children = mutableMapOf<String, Node>().also {
                    it[segment] = Node(
                        value = segment,
                        routeInfo = if (index == segments.lastIndex) routeInfo else null,
                        isWild = segment == "*"
                    )
                }
            } else if (node.children!![segment] == null) {
                node.children!![segment] = Node(
                    value = segment,
                    routeInfo = if (index == segments.lastIndex) routeInfo else null,
                    isWild = segment == "*"
                )
            }
            node = node.children!![segment]!!
        }
    }

    fun search(route: String): RouteInfo? {
        val uri = Uri.parse(route)
        val segments = mutableListOf(uri.scheme, uri.host, uri.pathSegments)
        var node = root
        segments.forEachIndexed { index, segment ->
            val expectNode = node.children?.get(segment) ?: node.children?.get("*") ?: return null
            if (index == segments.lastIndex) {
                return expectNode.routeInfo
            }
            node = expectNode
        }
        return null
    }

    class Node(
        val value: String, // 路由中由'/'分隔的部分
        val routeInfo: RouteInfo? = null,
        var children: MutableMap<String, Node>? = null,
        val isWild: Boolean // 是否是通配符节点
    ) {
        inline fun hasRoute() = routeInfo != null

        inline fun isLeaf() = children.isNullOrEmpty()
    }
}