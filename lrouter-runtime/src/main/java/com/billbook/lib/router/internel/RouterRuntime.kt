package com.billbook.lib.router.internel

import android.app.Application
import com.billbook.lib.router.*
import com.billbook.lib.router.generated.ModuleProvider

/**
 * @author xluotong@gmail.com
 */
internal object RouterRuntime : LRouter.Delegate {

    private lateinit var application: Application
    private val routeCentral: RouteCentral = DefaultRouteCentral()
    private val serviceCentral: ServiceCentral = DefaultServiceCentral()
    private lateinit var client: RouteClient

    fun initialize(application: Application) {
        this.application = application
        this.client = RouteClient.Builder()
            .context(application)
            .routeCentral(routeCentral)
            .build()
        // 加载路由和服务表
        initRoutes()
        LRouter.initialize(this)
    }

    private fun initRoutes() {
        ModuleProvider.modules().forEach { moduleContainer ->
            moduleContainer.withRoute { routeCentral.register(it) }
        }
    }

    override fun <T> getService(clazz: Class<T>): T? = serviceCentral[clazz]

    override fun <T> getService(clazz: Class<T>, name: String): T? = serviceCentral[clazz, name]

    override fun <T> getService(clazz: Class<T>, vararg params: Any): T? {
        return serviceCentral.getService(clazz, params)
    }

    override fun <T> getServiceProvider(clazz: Class<T>): ServiceProvider<T>? {
        TODO("Not yet implemented")
    }

    override fun newCall(request: Request): RouteCall = RealCall(client, request)

    override fun navigate(request: Request): Response {
        return newCall(request).execute()
    }
}