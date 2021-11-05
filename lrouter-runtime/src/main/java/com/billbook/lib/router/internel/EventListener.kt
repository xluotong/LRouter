package com.billbook.lib.router.internel

import com.billbook.lib.router.RouteCall

/**
 * @author xluotong@gmail.com
 */
abstract class EventListener {

    open fun onStart() {}

    open fun onRouteHit() {}

    open fun onFinished() {}

    fun interface Factory {
        fun create(call: RouteCall): EventListener
    }

    companion object {
        @JvmField
        val EMPTY: EventListener = object : EventListener() {}
    }
}

fun EventListener.asFactory() = EventListener.Factory { this }
