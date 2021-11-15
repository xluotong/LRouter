package com.billbook.lib.router.internel

import android.app.Application
import com.billbook.lib.router.*
import com.billbook.lib.router.generated.ModuleProvider

/**
 * @author xluotong@gmail.com
 */
internal object RouterInternal : LRouter.Delegate {

    private lateinit var application: Application
    private val routeCentral: RouteCentral = DefaultRouteCentral()
    private val serviceCentral: ServiceCentral = DefaultServiceCentral()
    private val injector = RouteInjector()
    private lateinit var routeContext: RouteContext

    fun initialize(application: Application) {
        this.application = application
        this.routeContext = RouteContext.Builder().context(application)
            .routeCentral(routeCentral)
            .serviceCentral(serviceCentral)
            .build()
        // 加载路由和服务表
        initRoutes()
        LRouter.initialize(this)
    }

    private fun initRoutes() {
        ModuleProvider.modules().forEach { container ->
            container.withRoutes { routeCentral.register(it) }
            container.withServices { serviceCentral.register(it) }
        }
    }

    override fun <T> getService(clazz: Class<T>): T? = serviceCentral[clazz]

    override fun <T> getService(clazz: Class<T>, name: String): T? = serviceCentral[clazz, name]

    override fun <T> getService(clazz: Class<T>, vararg params: Any): T? {
        return serviceCentral.getService(clazz, *params)
    }

    override fun <T> getServiceProvider(clazz: Class<T>): ServiceProvider<T>? {
        return serviceCentral.getServiceProvider(clazz)
    }

    override fun inject(any: Any) = injector.inject(any.javaClass, any)

    override fun findRoute(url: String): RouteInfo? {
        return navigateTo(
            Request.Builder.from(url).launchMode(Request.Mode.ROUTE_ONLY).build()
        ).routeInfo
    }

    override fun findRoute(target: Any): List<RouteInfo> = routeCentral[target::class.java]

    override fun newCall(request: Request): RouteCall = RealCall(routeContext, request)

    override fun navigateTo(request: Request): Response = newCall(request).execute()
}