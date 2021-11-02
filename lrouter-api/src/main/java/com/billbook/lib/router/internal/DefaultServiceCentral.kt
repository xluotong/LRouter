package com.billbook.lib.router.internal

import com.billbook.lib.router.CacheIn
import com.billbook.lib.router.ServiceInfo

/**
 * @author xluotong@gmail.com
 */
class DefaultServiceCentral : ServiceCentral, ServiceRegistryOwner {

    private val mServiceMap: MutableMap<Class<*>, MutableList<ServiceInfo<*>>> by lazy { hashMapOf() }
    private val mCache = mutableMapOf<Class<*>, Any?>()

    private fun <T> getServiceRow(clazz: Class<T>): ServiceInfo<T>? {
        val services = mServiceMap[clazz]
        return if (services.isNullOrEmpty()) null else services[0] as ServiceInfo<T>
    }

    private fun <T> getServiceRowByName(clazz: Class<T>, name: String): ServiceInfo<T>? {
        val services = mServiceMap[clazz]
        if (services.isNullOrEmpty()) return null
        return services.find { it.name == name } as? ServiceInfo<T>
    }

    @Synchronized
    override fun <T> getService(clazz: Class<T>): T? {
        val serviceRow = getServiceRow(clazz) ?: return null
        return when (serviceRow.cacheIn) {
            CacheIn.SINGLETON -> mCache.getOrPut(clazz) {
                serviceRow.service.tryNewInstance()
            } as? T
            CacheIn.UNDEFINED -> serviceRow.service.tryNewInstance()
        }
    }

    override fun <T> getService(clazz: Class<T>, name: String): T? {
        val serviceRow = getServiceRowByName(clazz, name) ?: return null
        return when (serviceRow.cacheIn) {
            CacheIn.SINGLETON -> mCache.getOrPut(clazz) {
                serviceRow.service.tryNewInstance()
            } as? T
            CacheIn.UNDEFINED -> serviceRow.service.tryNewInstance()
        }
    }

    override fun <T> getService(clazz: Class<T>, vararg params: Any): T? {
        val serviceRow = getServiceRow(clazz) ?: return null
        return when (serviceRow.cacheIn) {
            CacheIn.SINGLETON -> mCache.getOrPut(clazz) {
                serviceRow.service.tryNewInstance(params)
            } as? T
            CacheIn.UNDEFINED -> serviceRow.service.tryNewInstance(params)
        }
    }

    inner class DefaultRegistry : ServiceRegistry {
        override fun register(serviceInfo: ServiceInfo<*>) {
            mServiceMap.getOrPut(serviceInfo.definition) { arrayListOf() }
                .add(serviceInfo)
        }
    }

    override val registry: ServiceRegistry get() = DefaultRegistry()
}

internal fun <T> Class<*>.tryNewInstance(): T? = runCatching {
    return this.newInstance() as? T
}.getOrNull()

internal fun Array<out Any>.toClassArray(): Array<Class<*>> {
    return this.map { it.javaClass }.toTypedArray()
}

private fun <T> Class<*>.tryNewInstance(params: Array<out Any>): T? = runCatching {
    return this.getConstructor(*params.toClassArray()).also {
        it.isAccessible = true
    }.newInstance(params) as? T
}.getOrNull()