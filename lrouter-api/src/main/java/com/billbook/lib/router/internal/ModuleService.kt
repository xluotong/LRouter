package com.billbook.lib.router.internal


/**
 * 所有模块的Service中心
 */
interface ServiceCentral {

    val registry:ServiceRegistry

    fun <T> getService(clazz: Class<T>):T?

    fun <T> getServices(clazz:Class<T>):List<T>
}

interface ServiceRegistry {

    fun register(serviceInfo:ServiceInfo<*>)
}

enum class CacheIn{
    SINGLETON,
    UNDEFINED
}

data class ServiceInfo<T>(
    val moduleName:String,
    val definition:Class<out T>,
    val service:Class<T>,
    val name: String,
    val desc: String,
    val cacheIn: CacheIn
)

abstract class ServiceCollector {

    abstract fun onCollect(registry: ServiceRegistry)
}

//class ServiceCollectorImpl : ServiceCollector() {
//
//    override fun onCollect(registry: ServiceRegistry) {
//        registry.register(ServiceInfo("","",))
//    }
//}
