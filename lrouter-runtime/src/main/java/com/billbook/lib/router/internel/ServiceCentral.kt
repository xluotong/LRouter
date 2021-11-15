package com.billbook.lib.router.internel

import com.billbook.lib.router.ServiceInfo
import com.billbook.lib.router.ServiceProvider
import java.lang.reflect.Constructor

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
        return getServiceProvider(clazz)?.get()
    }

//    private fun <T> getOrPutInstance(
//        serviceInfo: ServiceInfo<*>,
//        producer: (ServiceInfo<*>) -> T
//    ): T? {
//        return when (serviceInfo.cacheIn) {
//            CacheIn.SINGLETON -> {
//                serviceCaches.getOrPut(serviceInfo.definition) {
//                    producer(serviceInfo)
//                } as? T
//            }
//            CacheIn.UNDEFINED -> {
//                producer(serviceInfo)
//            }
//        }
//    }

    @Synchronized
    override fun <T> getService(clazz: Class<T>, name: String): T? {
        return getServiceProvider(clazz)?.get(name)
    }

    @Synchronized
    override fun <T> getService(clazz: Class<T>, vararg params: Any): T? {
        return getServiceProvider(clazz)?.get(params)
    }

    @Synchronized
    override fun <T> getServiceProvider(clazz: Class<T>): ServiceProvider<T>? {
        return serviceTable[clazz]?.let {
            DefaultServiceProvider(clazz, it as List<ServiceInfo<T>>)
        }
    }
}

// TODO 支持迭代
internal class DefaultServiceProvider<T>(
    override val serviceClazz: Class<T>,
    private val services: List<ServiceInfo<T>>,
) : ServiceProvider<T> {
    override fun get(): T? = services.firstOrNull()?.service?.newInstance()

    override fun get(vararg params: Any): T? {
        this.services.forEach {
            val service = it.service.createInstance(*params)
            if (service != null) return service
        }
        return null
    }

    override fun get(name: String): T? {
        return services.find { it.name == name }?.newInstance()
    }
}

private inline fun <T> ServiceInfo<*>.newInstance(): T? {
    return runCatching { this.service.newInstance() as? T }.getOrNull()
}

private inline fun <T> Class<T>.createInstance(vararg params: Any): T? {
    return constructors.firstOrNull {
        runCatching { it.newInstance(*params) as T }.getOrNull() != null
    } as? T
}

