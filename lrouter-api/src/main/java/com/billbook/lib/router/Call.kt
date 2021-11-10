package com.billbook.lib.router

/**
 * @author xluotong@gmail.com
 */
interface RouteCall {
    /** Returns the original request that initiated this call. */
    fun request(): Request

    /**
     * Invokes the request immediately, and blocks until the response can be processed or is in error
     */
    fun execute(): Response

    /**
     * Returns true if this call has been either [executed][execute] or [enqueued][enqueue]. It is an
     * error to execute a call more than once.
     */
    fun isExecuted(): Boolean

    fun interface Factory {
        fun newCall(request: Request): RouteCall
    }
}