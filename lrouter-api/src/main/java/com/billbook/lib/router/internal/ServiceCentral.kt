package com.billbook.lib.router.internal

/**
 * @author xluotong@gmail.com
 */
interface ServiceCentral {

    fun <T> getService(clazz: Class<T>): T?

    fun <T> getService(clazz: Class<T>, name: String): T?

    fun <T> getService(clazz: Class<T>, vararg params: Any): T?

    operator fun <T> get(clazz: Class<T>): T? {
        return getService(clazz)
    }
}