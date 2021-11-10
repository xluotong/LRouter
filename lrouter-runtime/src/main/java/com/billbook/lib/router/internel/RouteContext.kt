package com.billbook.lib.router.internel

import android.content.Context
import com.billbook.lib.router.EventListener

/**
 * @author xluotong@gmail.com
 */
internal class RouteContext internal constructor(builder: Builder) {

    @get:JvmName("appContext")
    val appContext: Context = builder.appContext

    @get:JvmName("eventListenerFactory")
    val eventListenerFactory: EventListener.Factory = builder.eventListenerFactory

    @get:JvmName("routeCentral")
    val routeCentral: RouteCentral = builder.routeCentral

    @get:JvmName("serviceCentral")
    val serviceCentral: ServiceCentral = builder.serviceCentral

    class Builder {
        internal lateinit var appContext: Context
        internal var eventListenerFactory: EventListener.Factory = EventListener.EMPTY.asFactory()
        internal lateinit var routeCentral: RouteCentral
        internal lateinit var serviceCentral: ServiceCentral

        fun context(appContext: Context) = apply {
            this.appContext = appContext
        }

        fun eventListenerFactory(factory: EventListener.Factory) = apply {
            this.eventListenerFactory = factory
        }

        fun routeCentral(routeCentral: RouteCentral) = apply {
            this.routeCentral = routeCentral
        }

        fun serviceCentral(serviceCentral: ServiceCentral) = apply {
            this.serviceCentral = serviceCentral
        }

        fun build(): RouteContext = RouteContext(this)
    }
}

internal fun EventListener.asFactory() = EventListener.Factory { this }
