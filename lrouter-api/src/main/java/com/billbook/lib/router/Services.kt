package com.billbook.lib.router

import javax.inject.Provider

enum class CacheIn {
    SINGLETON,
    UNDEFINED
}

data class ServiceInfo<T>(
    val definition: Class<out T>,
    val service: Class<T>,
    val name: String,
    val desc: String,
    val cacheIn: CacheIn
)

/**
 * Useful if it is lazy create service
 */
interface ServiceProvider<T> : Provider<T> {

    val serviceClazz:Class<T>

    fun get(vararg params: Any): T?

    fun get(name: String): T?
}


