package com.billbook.lib.router

import org.gradle.api.Action
import org.gradle.api.Task

/**
 * @author xluotong@gmail.com
 */
abstract class NamedPreConfigureAction<T : Task> : Action<T> {

    abstract val name: String

    open fun preConfigure(taskName: String) {

    }

    override fun execute(t: T) {

    }
}