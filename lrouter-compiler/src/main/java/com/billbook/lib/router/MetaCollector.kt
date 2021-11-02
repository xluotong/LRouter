package com.billbook.lib.router

import okio.ByteString.Companion.encodeUtf8

/**
 * @author xluotong@gmail.com
 */
interface MetaCollector {

    val hasMeta: Boolean
    val moduleMeta: ModuleMeta

    fun addService(meta: ServiceMeta)

    fun addServices(metas: List<ServiceMeta>)

    fun addRoute(meta: RouteMeta)

    fun addRoutes(metas: List<RouteMeta>)
}

class MetaCollectorImpl : MetaCollector {
    private val _serviceMetas = mutableListOf<ServiceMeta>()
    private val _routeMetas = mutableListOf<RouteMeta>()
    override val hasMeta: Boolean get() = _routeMetas.isNotEmpty() || _serviceMetas.isNotEmpty()

    override val moduleMeta: ModuleMeta
        get() {
            val moduleName = requireNotNull(moduleName())
            return ModuleMeta(
                moduleName,
                 String.format(SERVICE_CONTAINER_CLASS_NAME_FORMAT, moduleName.capitalize()),
                _routeMetas,
                _serviceMetas
            )
        }

    override fun addService(meta: ServiceMeta) {
        _serviceMetas.add(meta)
    }

    override fun addRoute(meta: RouteMeta) {
        _routeMetas.add(meta)
    }

    override fun addServices(metas: List<ServiceMeta>) {
        _serviceMetas.addAll(metas)
    }

    override fun addRoutes(metas: List<RouteMeta>) {
        _routeMetas.addAll(metas)
    }

    private fun moduleName(): String? {
        if (_serviceMetas.isNotEmpty()) return _serviceMetas.first().service.asModuleName()
        if (_routeMetas.isNotEmpty()) return _routeMetas.first().definition.asModuleName()
        return null
    }
}

internal fun String.asModuleName() = "lr" + this.encodeUtf8().sha1().hex()
