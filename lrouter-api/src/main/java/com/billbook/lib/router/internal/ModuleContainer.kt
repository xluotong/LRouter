package com.billbook.lib.router.internal

import com.billbook.lib.router.Module
import com.billbook.lib.router.RouteInfo
import com.billbook.lib.router.ServiceInfo

/**
 * @author xluotong@gmail.com
 */
abstract class ModuleContainer(private val module: Module) : Module by module {

    abstract fun getServices(): List<ServiceInfo<*>>

    abstract fun getRoutes(): List<RouteInfo>

    fun withServices(action: (service: ServiceInfo<*>) -> Unit) = apply {
        getServices()?.forEach { action.invoke(it) }
    }

    fun withRoutes(action: (route: RouteInfo) -> Unit) = apply {
        getRoutes()?.forEach { action.invoke(it) }
    }
}
