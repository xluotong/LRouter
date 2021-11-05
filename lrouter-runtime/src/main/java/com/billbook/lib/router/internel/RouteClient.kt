package com.billbook.lib.router.internel

/**
 * @author xluotong@gmail.com
 */
internal class RouteClient internal constructor(builder: Builder) {

    @get:JvmName("eventListenerFactory")
    val eventListenerFactory: EventListener.Factory = builder.eventListenerFactory
    @get:JvmName("routeCentral")
    val routeCentral: RouteCentral = builder.routeCentral

    class Builder constructor() {
        internal var eventListenerFactory: EventListener.Factory = EventListener.EMPTY.asFactory()
        internal lateinit var routeCentral:RouteCentral

        fun eventListenerFactory(factory:EventListener.Factory) = apply {
            this.eventListenerFactory = factory
        }

        fun routeCentral(routeCentral:RouteCentral) = apply {
            this.routeCentral = routeCentral
        }

        fun build(): RouteClient = RouteClient(this)
    }
}