package com.billbook.lib.router.internal

import com.billbook.lib.router.DefaultModule
import com.billbook.lib.router.Module
import com.billbook.lib.router.RouteInfo
import com.billbook.lib.router.ServiceInfo
import java.util.*

/**
 * @author xluotong@gmail.com
 */
abstract class ModuleContainer(private val module: Module) : Module by module {

    abstract fun getServices(): List<ServiceInfo<*>>

    abstract fun getRoutes(): List<RouteInfo>

    fun withServices(action: (services: List<ServiceInfo<*>>) -> Unit) = apply {
        action.invoke(getServices())
    }

    fun withRoutes(action: (routes: List<RouteInfo>) -> Unit) = apply {
        action.invoke(getRoutes())
    }

    fun withService(action: (service: ServiceInfo<*>) -> Unit) = apply {
        getServices()?.forEach { action.invoke(it) }
    }

    fun withRoute(action: (route: RouteInfo) -> Unit) = apply {
        getRoutes()?.forEach { action.invoke(it) }
    }
}

class AModuleContainer : ModuleContainer {

    constructor() : super(DefaultModule("A", null))

    override fun getServices(): List<ServiceInfo<*>> {
        Collections.emptyList<ServiceInfo<*>>()
        return listOf()
    }

    override fun getRoutes(): List<RouteInfo> {
        return listOf()
    }
}

class BModuleContainer : ModuleContainer {

    constructor() : super(DefaultModule("B", null))

    override fun getServices(): List<ServiceInfo<*>> {
        Collections.emptyList<ServiceInfo<*>>()
        return listOf()
    }

    override fun getRoutes(): List<RouteInfo> {
        return listOf()
    }
}