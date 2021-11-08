package com.billbook.lib.lrouter

import androidx.test.ext.junit.runners.AndroidJUnit4
import com.billbook.lib.router.RouteInfo
import com.billbook.lib.router.RouteType
import com.billbook.lib.router.internel.RouteTree
import org.junit.Test
import org.junit.runner.RunWith

/**
 * @author xluotong@gmail.com
 */
@RunWith(AndroidJUnit4::class)
class RouteTreeTest {

    @Test
    fun testFindRoute() {
        val routeTree = RouteTree()
        routeTree.add(
            RouteInfo(
                "/user/*", "", "", "", "", "",
                RouteTreeTest::class.java, RouteType.ACTIVITY, null
            )
        )
        routeTree.add(
            RouteInfo(
                "/user/login", "", "", "", "", "",
                RouteTreeTest::class.java, RouteType.ACTIVITY, null
            )
        )
        routeTree.add(
            RouteInfo(
                "/user/register", "billbook", "user", "", "", "",
                RouteTreeTest::class.java, RouteType.ACTIVITY, null
            )
        )
        routeTree.add(
            RouteInfo(
                "/user/logout", "billbook", "user", "", "", "",
                RouteTreeTest::class.java, RouteType.ACTIVITY, null
            )
        )
        routeTree.add(
            RouteInfo(
                "/bill/main", "billbook", "bill", "", "", "",
                RouteTreeTest::class.java, RouteType.ACTIVITY, null
            )
        )
        routeTree.add(
            RouteInfo(
                "/bill/detail", "billbook", "bill", "", "", "",
                RouteTreeTest::class.java, RouteType.ACTIVITY, null
            )
        )
        routeTree.add(
            RouteInfo(
                "/chat/detail", "billbook", "chat", "", "", "",
                RouteTreeTest::class.java, RouteType.ACTIVITY, null
            )
        )
        assert(routeTree.findRoute("/user/login")?.path == "/user/login")
        assert(routeTree.findRoute("/user/test")?.path == "/user/*")
        assert(routeTree.findRoute("billbook://user/user/register")?.scheme == "billbook")
    }
}