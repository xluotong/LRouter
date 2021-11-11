package com.billbook.lib.router.annotation

/**
 * Indicates that there is an internal complementary protocol to the public type that is annotated with this.
 *
 * This should only be used on a type that is always assumed to also implement the internal protocol by LRouter internals.
 *
 * This exists to help anyone reading the source code realise that there is an internal component to the type.
 */
@Retention(AnnotationRetention.SOURCE)
@Target(AnnotationTarget.TYPE)
annotation class HasInternalProtocol
