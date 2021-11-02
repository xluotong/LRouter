package com.billbook.lib.router.internal

import com.billbook.lib.router.internal.generated.ModuleProvider

/**
 * @author xluotong@gmail.com
 */
internal interface RouteContext {
    val routeRegistry: RouteRegistry
    val serviceRegistry: ServiceRegistry
    val serviceCentral: ServiceCentral
}

internal class RouteContextImpl : RouteContext {
    private val _serviceCentral = DefaultServiceCentral()

    override val routeRegistry: RouteRegistry
        get() = TODO("Not yet implemented")
    override val serviceRegistry: ServiceRegistry
        get() = _serviceCentral.registry
    override val serviceCentral: ServiceCentral
        get() = _serviceCentral

    init {
        ModuleProvider.modules().forEach { container ->
            container.withServices { services ->
                services.forEach {
                    serviceRegistry.register(it)
                }
            }.withRoutes { routes ->
                routes.forEach {
                    routeRegistry.register(it)
                }
            }
        }
    }
}