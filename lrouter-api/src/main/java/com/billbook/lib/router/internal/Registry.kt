package com.billbook.lib.router.internal

import com.billbook.lib.router.RouteInfo
import com.billbook.lib.router.ServiceInfo

/**
 * @author xluotong@gmail.com
 */
interface RouteRegistry {
    fun register(routeInfo: RouteInfo)
}

interface ServiceRegistry {
    fun register(serviceInfo: ServiceInfo<*>)
}

interface ServiceRegistryOwner {
    val registry: ServiceRegistry
}

