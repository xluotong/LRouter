package com.billbook.lib.router

/**
 * @author xluotong@gmail.com
 */
interface Injector<T> {

    fun inject(target: T)
}