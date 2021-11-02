package com.billbook.lib.router.internal

/**
 * @author xluotong@gmail.com
 */
class ModuleRegistry(private val mServiceRegistry: ServiceRegistry) : ServiceRegistry {

    fun init() {
        ModuleProvider.serviceContainers().forEach {
            runCatching { Class.forName(it).newInstance() as ServiceContainer }.getOrNull()
                ?.onRegister(mServiceRegistry)
        }
    }

    override fun register(serviceInfo: ServiceInfo<*>) {
        mServiceRegistry.register(serviceInfo)
    }
}

