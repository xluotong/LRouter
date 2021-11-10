package com.billbook.lib.router.annotation

import com.billbook.lib.router.Interceptor
import com.billbook.lib.router.Launcher
import kotlin.reflect.KClass

internal const val EMPTY = ""

@Retention(AnnotationRetention.SOURCE)
@Target(AnnotationTarget.CLASS)
annotation class Routes(
    val routes: Array<Route>,
    val name: String = EMPTY,
    val desc: String = EMPTY,
    val interceptors: Array<KClass<out Interceptor>> = [],
    val launcher: KClass<out Launcher> = Launcher::class
)

@Retention(AnnotationRetention.SOURCE)
@Target(AnnotationTarget.CLASS)
annotation class Route(
    val path: String,
    val scheme: String = EMPTY,
    val host: String = EMPTY,
    val desc: String = EMPTY,
    val interceptors: Array<KClass<out Interceptor>> = []
)