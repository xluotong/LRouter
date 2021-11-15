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
    private val serviceCache: ServiceCache = ServiceCacheImpl()

    @Synchronized
    override fun register(serviceInfo: ServiceInfo<*>) {
        serviceTable.getOrPut(serviceInfo.declaring) { mutableListOf() }.add(serviceInfo)
    }

    @Synchronized
    override fun <T> getService(clazz: Class<T>): T? {
        return getServiceProvider(clazz)?.get()
    }

    @Synchronized
    override fun <T> getService(clazz: Class<T>, name: String): T? {
        return getServiceProvider(clazz)?.get(name)
    }

    @Synchronized
    override fun <T> getService(clazz: Class<T>, vararg params: Any): T? {
        return getServiceProvider(clazz)?.get(*params)
    }

    @Synchronized
    override fun <T> getServiceProvider(clazz: Class<T>): ServiceProvider<T>? {
        return serviceTable[clazz]?.let {
            DefaultServiceProvider(clazz, it as List<ServiceInfo<T>>, serviceCache)
        }
    }

    private class ServiceCacheImpl : ServiceCache {
        private val serviceCaches = mutableMapOf<Class<*>, Any?>()

        override fun <T> getOrPut(info: ServiceInfo<T>, producer: () -> T?): T? {
            return when (info.cacheIn) {
                CacheIn.SINGLETON -> {
                    synchronized(serviceCaches) {
                        serviceCaches.getOrPut(
                            info.service,
                            producer
                        ) as? T
                    }
                }
                CacheIn.UNDEFINED -> producer()
            }
        }
    }
}


internal interface ServiceCache {
    fun <T> getOrPut(info: ServiceInfo<T>, producer: () -> T?): T?
}

internal class DefaultServiceProvider<T>(
    override val declaringClass: Class<T>,
    private val services: List<ServiceInfo<T>>,
    private val cache: ServiceCache
) : ServiceProvider<T> {
    override fun get(): T? = services.firstOrNull()?.service?.newInstance()

    override fun get(vararg params: Any): T? {
        return this.services.mapOrNull {
            cache.getOrPut(it) { it.service.createInstance(it.declaring, *params) }
        }
    }

    override fun get(name: String): T? {
        return services.find { it.name == name }
            ?.let { cache.getOrPut(it) { it.newInstance() } }
    }

    override fun get(name: String, vararg params: Any): T? {
        return services.find { it.name == name }
            ?.let {
                cache.getOrPut(it) {
                    it.service.createInstance(it.declaring, params)
                }
            }
    }

    override fun iterator(): Iterator<T?> = ServiceIterator(services.iterator(), cache)

    class ServiceIterator<T>(
        private val infoIterator: Iterator<ServiceInfo<T>>,
        private val cache: ServiceCache
    ) : Iterator<T?> {

        override fun hasNext(): Boolean = infoIterator.hasNext()

        override fun next(): T? {
            return infoIterator.next()?.let { info ->
                cache.getOrPut(info) { info.newInstance() }
            }
        }
    }
}

private inline fun <T> ServiceInfo<T>.newInstance(): T? {
    return try {
        declaring.cast(this.service.newInstance())
    } catch (e: Throwable) {
        null
    }
}

private inline fun <T> Class<out T>.createInstance(
    declaringClass: Class<T>,
    vararg params: Any
): T? {
    return constructors.mapOrNull {
        try {
            declaringClass.cast(it.newInstance(*params))
        } catch (e: Throwable) {
            null
        }
    }
}

internal inline fun <T, R> Array<out T>.mapOrNull(transform: (T) -> R?): R? {
    for (element in this) {
        val result = transform(element)
        if (result != null) return result
    }
    return null
}

internal inline fun <T, R> List<out T>.mapOrNull(transform: (T) -> R?): R? {
    for (element in this) {
        val result = transform(element)
        if (result != null) return result
    }
    return null
}

