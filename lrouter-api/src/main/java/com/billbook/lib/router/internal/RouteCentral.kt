package com.billbook.lib.router.internal

import com.billbook.lib.router.RouteInfo

/**
 * @author xluotong@gmail.com
 */
internal interface RouteCentral : RouteRegistry {

    fun findRoute(uri: String): RouteInfo?

    companion object {
        fun get(): RouteCentral = DefaultRouteCentral()
    }
}

internal class DefaultRouteCentral : RouteCentral {

    // *://*/user/login
    // a://xxx/user/login
    // b://xxx/user/login
    // /login/regiser -> 只有path
    // user/login/regiser -> 有host + path
    // billbook://user/login/register/view/detail/{id} -> 都有
    // billbook://user/login/register/view/detail/1?a=b&c=d -> 都有

    override fun findRoute(uri: String): RouteInfo? {

    }

    override fun register(routeInfo: RouteInfo) {

    }
}