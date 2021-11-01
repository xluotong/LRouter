package com.billbook.lib.router

import com.billbook.lib.router.internal.ModuleRegistry
import com.billbook.lib.router.internal.ServiceCentralImpl

object LRouter {

    private val serviceCentral by lazy { ServiceCentralImpl() }
    private val registry: ModuleRegistry by lazy { ModuleRegistry(serviceCentral) }

    fun init() {
        registry.init()
    }

    fun <T> getService(clazz: Class<T>): T? {
        return serviceCentral.getService(clazz)
    }
}