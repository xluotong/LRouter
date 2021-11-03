package com.billbook.lib.router.annotation

import com.billbook.lib.router.interceptor.Interceptor
import kotlin.reflect.KClass

private const val DEFAULT_GROUP = "Default"

@Retention(AnnotationRetention.SOURCE)
@Target(AnnotationTarget.CLASS)
annotation class Routes(
    val routes:Array<Route>,
    val name:String = DEFAULT_GROUP,
    val desc:String = "",
    val interceptors:Array<KClass<out Interceptor>> = []
)

@Retention(AnnotationRetention.SOURCE)
@Target(AnnotationTarget.CLASS)
annotation class Route(
    val path:String,
    val scheme:String = "",
    val host:String = "",
    val desc:String = "",
    val interceptors:Array<KClass<out Interceptor>> = []
)