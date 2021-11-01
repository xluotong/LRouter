package com.billbook.lib.router.annotation

import kotlin.reflect.KClass

@Retention(AnnotationRetention.SOURCE)
@Target(AnnotationTarget.CLASS)
annotation class Services(
    val value:Array<KClass<*>> = [],
    val services:Array<Service> = [],
)

@Retention(AnnotationRetention.SOURCE)
@Target(AnnotationTarget.CLASS)
annotation class Service(
    val value:KClass<*>,
    val name:String,
    val desc:String
)
