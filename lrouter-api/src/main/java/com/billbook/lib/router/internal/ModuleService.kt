package com.billbook.lib.router.internal

interface ServiceCentral : ServiceRegistry {

    fun <T> getService(clazz: Class<T>): T?

    fun <T> getService(clazz: Class<T>, name: String): T?

    fun <T> getService(clazz: Class<T>, vararg params: Any): T?

    operator fun <T> get(clazz: Class<T>): T? {
        return getService(clazz)
    }
}

interface ServiceRegistry {

    fun register(serviceInfo: ServiceInfo<*>)
}

enum class CacheIn {
    SINGLETON,
    UNDEFINED
}

data class ServiceInfo<T>(
    val moduleName: String,
    val definition: Class<out T>,
    val service: Class<T>,
    val name: String,
    val desc: String,
    val cacheIn: CacheIn
)

abstract class ServiceContainer {

    abstract fun onRegister(registry: ServiceRegistry)
}
