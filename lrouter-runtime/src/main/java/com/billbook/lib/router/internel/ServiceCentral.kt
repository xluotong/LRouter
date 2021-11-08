package com.billbook.lib.router.internel

import com.billbook.lib.router.CacheIn
import com.billbook.lib.router.ServiceInfo
import com.billbook.lib.router.ServiceProvider

/**
 * @author xluotong@gmail.com
 */
internal interface ServiceCentral {

    fun register(serviceInfo: ServiceInfo<*>)

    fun <T> getService(clazz: Class<T>): T?

    fun <T> getService(clazz: Class<T>, name: String): T?

    fun <T> getService(clazz: Class<T>, vararg params: Any): T?

    fun <T> getServiceProvider(clazz: Class<T>): ServiceProvider<T>?

    operator fun <T> get(clazz: Class<T>): T? = getService(clazz)

    operator fun <T> get(clazz: Class<T>, name: String): T? = getService(clazz, name)
}

internal class DefaultServiceCentral : ServiceCentral {

    private val serviceTable: MutableMap<Class<*>, MutableList<ServiceInfo<*>>> = mutableMapOf()
    private val serviceCaches = mutableMapOf<Class<*>, Any?>()

    @Synchronized
    override fun register(serviceInfo: ServiceInfo<*>) {
        serviceTable.getOrPut(serviceInfo.definition) { mutableListOf() }.add(serviceInfo)
    }

    @Synchronized
    override fun <T> getService(clazz: Class<T>): T? {
        return serviceTable[clazz]?.first()
            ?.let { service ->
                getOrPutInstance(service) { it.newInstance() }
            }
    }

    private fun <T> getOrPutInstance(
        serviceInfo: ServiceInfo<*>,
        producer: (ServiceInfo<*>) -> T
    ): T? {
        return when (serviceInfo.cacheIn) {
            CacheIn.SINGLETON -> {
                serviceCaches.getOrPut(serviceInfo.definition) {
                    producer(serviceInfo)
                } as? T
            }
            CacheIn.UNDEFINED -> {
                producer(serviceInfo)
            }
        }
    }

    @Synchronized
    override fun <T> getService(clazz: Class<T>, name: String): T? {
        return serviceTable[clazz]?.firstOrNull { it.name == name }?.newInstance<T>()
    }

    @Synchronized
    override fun <T> getService(clazz: Class<T>, vararg params: Any): T? {
        return serviceTable[clazz]?.first()
            ?.let { service ->
                getOrPutInstance(service) { it.newInstance(params) }
            }
    }

    override fun <T> getServiceProvider(clazz: Class<T>): ServiceProvider<T>? {
        TODO()
    }
}

internal class DefaultServiceProvider<T>(
    private val services: List<ServiceInfo<T>>
) : ServiceProvider<T> {

    private val iterator: Iterator<ServiceInfo<T>> = services.iterator()

    override fun hasNext(): Boolean = iterator.hasNext()

    override fun next(): T? = iterator.next()?.newInstance()

    override fun get(): T? = services[0].service.newInstance()

    override fun get(vararg params: Any): T? = services[0].newInstance(params)

    override fun get(name: String): T? {
        return services.find { it.name == name }?.newInstance()
    }
}

private inline fun <T> ServiceInfo<*>.newInstance(): T? {
    return runCatching { this.service.newInstance() as? T }.getOrNull()
}

private inline fun <T> ServiceInfo<*>.newInstance(vararg params: Any): T? {
    return runCatching {
        this.service.getDeclaredConstructor(*params.map { it::class.java }
            .toTypedArray()).newInstance(params) as? T
    }.getOrNull()
}
