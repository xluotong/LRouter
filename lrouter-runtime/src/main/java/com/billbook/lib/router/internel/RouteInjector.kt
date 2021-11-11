package com.billbook.lib.router.internel

import android.util.LruCache
import com.billbook.lib.router.Injector

/**
 * @author xluotong@gmail.com
 */
internal class RouteInjector {

    private val injectors = object : LruCache<String, Injector<Any>>(64) {
        override fun create(key: String): Injector<Any>? {
            return runCatching {
                Class.forName("key\$\$RouterInjector")
                    .asSubclass(Injector::class.java)
                    .newInstance() as Injector<Any>
            }.getOrNull()
        }
    }

    fun <T : Any> inject(clazz: Class<T>, target: T) {
        var targetClass: Class<in T> = clazz
        while (targetClass != Any::class.java) {
            injectors.get(clazz.name)?.let {
                if (targetClass != clazz) {
                    injectors.put(clazz.name, it)
                }
                it.inject(target)
                return
            }
            targetClass = clazz.superclass ?: break
        }
    }
}