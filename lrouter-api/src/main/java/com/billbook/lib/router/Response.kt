package com.billbook.lib.router

import androidx.fragment.app.Fragment

/**
 * @author xluotong@gmail.com
 */
interface ResponseBuilder<T> {
    fun code(code: Response.Code): T
    fun message(message: String): T
    fun routeInfo(routeInfo: RouteInfo?): T
    fun fragment(fragment: Fragment?): T
    fun build(): Response
}

class Response private constructor(builder: Builder) {
    val code: Code = builder.code
    val message: String? = builder.message
    val routeInfo: RouteInfo? = builder.routeInfo
    var fragment:Fragment? = builder.fragment

    fun newBuilder() = Builder().code(code)
        .routeInfo(routeInfo)

    class Builder : ResponseBuilder<Builder> {

        internal lateinit var code: Code
        internal var routeInfo: RouteInfo? = null
        internal var message: String? = null
        internal var fragment: Fragment? = null

        override fun code(code: Code): Builder = apply {
            this.code = code
        }

        override fun message(message: String): Builder = apply {
            this.message = message
        }

        override fun routeInfo(routeInfo: RouteInfo?): Builder = apply {
            this.routeInfo = routeInfo
        }

        override fun fragment(fragment: Fragment?): Builder = apply {
            this.fragment = fragment
        }

        override fun build(): Response = Response(this)
    }

    enum class Code(val message: String) {
        OK("Ok"),
        NOF_FOUND("Route info not found!"),
        REFUSE("Route request is refuse!"),
        UNAUTHORIZED("Route request is unauthorized!"),
        CONFLICT("Route match conflict!"),
        CANCEL("Cancel");

        override fun toString(): String {
            return "Code(code = ${this.ordinal}, message = ${this.message})"
        }
    }
}