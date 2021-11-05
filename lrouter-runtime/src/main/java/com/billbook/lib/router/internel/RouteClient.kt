package com.billbook.lib.router.internel

/**
 * @author xluotong@gmail.com
 */
class RouteClient internal constructor(builder: Builder) {

    @get:JvmName("eventListenerFactory")
    val eventListenerFactory: EventListener.Factory = builder.eventListenerFactory

    class Builder constructor() {
        internal var eventListenerFactory: EventListener.Factory = EventListener.EMPTY.asFactory()

        fun build(): RouteClient = RouteClient(this)
    }
}