package com.billbook.lib.router

import javax.inject.Provider

enum class CacheIn {
    SINGLETON,
    UNDEFINED
}

data class ServiceInfo<T>(
    val declaring: Class<T>,
    val service: Class<out T>,
    val name: String,
    val desc: String,
    val cacheIn: CacheIn
)

/**
 * Useful if it is lazy create service
 */
interface ServiceProvider<T> : Provider<T>, Iterable<T?> {

    val declaringClass: Class<T>

    fun get(vararg params: Any): T?

    fun get(name: String): T?

    fun get(name: String, vararg params: Any): T?
}


