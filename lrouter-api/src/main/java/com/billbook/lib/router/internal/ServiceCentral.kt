package com.billbook.lib.router.internal

/**
 * @author xluotong@gmail.com
 */
interface ServiceCentral {

    fun <T> getService(clazz: Class<T>): T?

    fun <T> getService(clazz: Class<T>, name: String): T?

    fun <T> getService(clazz: Class<T>, vararg params: Any): T?

    fun <T> getServiceProvider(clazz: Class<T>): ServiceProvider<T>

    operator fun <T> get(clazz: Class<T>): T? = getService(clazz)
}

/**
 * Useful if it is lazy create service
 */
interface ServiceProvider<T> : Iterator<T> {
    fun get(): T

    fun get(vararg params: Any): T

    fun get(name: String)
}