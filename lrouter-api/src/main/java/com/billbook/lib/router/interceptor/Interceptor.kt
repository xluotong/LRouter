package com.billbook.lib.router.interceptor

import com.billbook.lib.router.Request
import com.billbook.lib.router.Response

fun interface Interceptor {

    fun intercept(chain: Chain):Response

    companion object {
        /**
         * Constructs an interceptor for a lambda. This compact syntax is most useful for inline
         * interceptors.
         *
         * ```
         * val interceptor = Interceptor { chain: Interceptor.Chain ->
         *     chain.proceed(chain.request())
         * }
         * ```
         */
        inline operator fun invoke(crossinline block: (chain: Chain) -> Response): Interceptor =
            Interceptor { block(it) }
    }

    interface Chain {
        fun request():Request
        fun proceed(request: Request):Response
    }
}