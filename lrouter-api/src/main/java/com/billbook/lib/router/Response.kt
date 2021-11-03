package com.billbook.lib.router

/**
 * @author xluotong@gmail.com
 */
interface ResponseBuilder<T> {
    fun code(code: Response.Code): T
    fun routeInfo(routeInfo: RouteInfo?): T
    fun build(): Response
}

class Response private constructor(builder: Builder) {
    val code:Code = builder.code
    val routeInfo:RouteInfo? = builder.routeInfo

    fun newBuilder() = Builder().code(code)
        .routeInfo(routeInfo)

    class Builder : ResponseBuilder<Builder> {

        internal lateinit var code: Code
        internal var routeInfo: RouteInfo? = null

        override fun code(code: Code): Builder {
            this.code = code
            return this
        }

        override fun routeInfo(routeInfo: RouteInfo?): Builder {
            this.routeInfo = routeInfo
            return this
        }

        override fun build(): Response = Response(this)
    }

    enum class Code(val message: String) {
        SUCCESS("Success"),
        ROUTE_NOF_FOUND("Route info not found!"),
        CANCEL("Cancel");

        override fun toString(): String {
            return "Code(code = ${this.ordinal}, message = ${this.message})"
        }
    }
}