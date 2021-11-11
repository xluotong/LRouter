package com.billbook.lib.router.annotation

/**
 * @author xluotong@gmail.com
 */
@Target(AnnotationTarget.FIELD)
@Retention(AnnotationRetention.SOURCE)
annotation class Autowired(
    val name: String = EMPTY,
    val required: Boolean = false
)