package com.billbook.lib.router.annotation

/**
 * @author xluotong@gmail.com
 */
@Target(AnnotationTarget.FIELD)
@Retention(AnnotationRetention.SOURCE)
annotation class Query(
    val value: String = EMPTY,
)