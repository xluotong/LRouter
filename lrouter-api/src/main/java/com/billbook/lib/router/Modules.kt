package com.billbook.lib.router

/**
 * @author xluotong@gmail.com
 */
interface Module {
    val name: String
    val aware: Class<*>?
}

fun interface ModuleAware {
    fun apply()
}

data class DefaultModule(override val name: String, override val aware: Class<*>?) : Module