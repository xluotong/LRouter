package com.billbook.lib.router.internal

import com.billbook.lib.router.ServiceInfo
import java.util.*

/**
 * @author xluotong@gmail.com
 */
interface Module {
    val name: String
    val aware: String
}

interface ModuleAware {
    fun apply()
}

data class DefaultModule(override val name: String, override val aware: String) : Module

abstract class ModuleContainer(private val module: Module) : Module by module {

    abstract fun getServices(): List<ServiceInfo<*>>

    abstract fun getRoutes(): List<RouteInfo>

    fun withServices(action: (services: List<ServiceInfo<*>>) -> Unit) = apply {
        action.invoke(getServices())
    }

    fun withRoutes(action: (routes: List<RouteInfo>) -> Unit) = apply {
        action.invoke(getRoutes())
    }
}

class AModuleContainer : ModuleContainer {

    constructor() : super(DefaultModule("A", ""))

    override fun getServices(): List<ServiceInfo<*>> {
        Collections.emptyList<ServiceInfo<*>>()
        return listOf()
    }

    override fun getRoutes(): List<RouteInfo> {
        return listOf()
    }
}

class BModuleContainer : ModuleContainer {

    constructor() : super(DefaultModule("B", ""))

    override fun getServices(): List<ServiceInfo<*>> {
        Collections.emptyList<ServiceInfo<*>>()
        return listOf()
    }

    override fun getRoutes(): List<RouteInfo> {
        return listOf()
    }
}