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
            .filter { it.isNotEmpty() }
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

    fun findRoute(uri: Uri): RouteInfo? {
        val segments = uri.toRouteSegments()
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

    fun findRoute(targetClass: Class<*>): List<RouteInfo> {
        return mutableListOf<RouteInfo>().also {
            findRouteInternal(targetClass, root, it)
        }
    }

    private fun findRouteInternal(
        targetClass: Class<*>,
        node: Node,
        results: MutableList<RouteInfo>
    ) {
        node.routeInfo?.let { if (it.targetClass == targetClass) results.add(it) }
        node.children?.values?.forEach { node ->
            findRouteInternal(targetClass, node, results)
        }
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

private inline fun Uri.toRouteSegments(): List<String> {
    val segments = mutableListOf<String>()
    segments.add(scheme ?: EMPTY_STRING)
    segments.add(host ?: EMPTY_STRING)
    segments.addAll(pathSegments)
    return segments
}