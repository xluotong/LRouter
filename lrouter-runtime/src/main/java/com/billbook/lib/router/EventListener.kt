package com.billbook.lib.router

/**
 * @author xluotong@gmail.com
 */
abstract class EventListener {

    open fun onStart() {}

    open fun onRouteHit() {}

    open fun onLaunchStart() {}

    open fun onLaunchEnd() {}

    open fun onFinished() {}

    fun interface Factory {
        fun create(call: RouteCall): EventListener
    }

    companion object {
        @JvmField
        internal val EMPTY: EventListener = object : EventListener() {}
    }
}