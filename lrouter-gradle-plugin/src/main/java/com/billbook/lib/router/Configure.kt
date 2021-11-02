package com.billbook.lib.router

import org.gradle.api.Action

/**
 * @author xluotong@gmail.com
 */
interface NamedAction<T> : Action<T> {
    val name:String
}