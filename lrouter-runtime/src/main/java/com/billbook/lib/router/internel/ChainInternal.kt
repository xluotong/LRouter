package com.billbook.lib.router.internel

import com.billbook.lib.router.Interceptor
import com.billbook.lib.router.RouteInfo

/**
 * @author xluotong@gmail.com
 */
interface ChainInternal : Interceptor.Chain {
    val route: RouteInfo
}