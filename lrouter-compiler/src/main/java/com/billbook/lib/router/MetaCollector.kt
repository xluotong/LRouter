package com.billbook.lib.router

import okio.ByteString.Companion.encodeUtf8

/**
 * @author xluotong@gmail.com
 */
interface MetaCollector {

    val hasMeta: Boolean
    val moduleMeta: ModuleMeta

    fun addInjectField(target: InjectMeta.Target, field: InjectMeta.Field)

    fun addAutowiredField(target: InjectMeta.Target, field: InjectMeta.Field)

    fun addService(meta: ServiceMeta)

    fun addServices(metas: List<ServiceMeta>)

    fun addRoute(meta: RouteMeta)

    fun addRoutes(metas: List<RouteMeta>)
}

class MetaCollectorImpl : MetaCollector {
    private val _serviceMetas = mutableListOf<ServiceMeta>()
    private val _routeMetas = mutableListOf<RouteMeta>()
    private val _injectMetas = mutableMapOf<String, InjectMeta>()
    override val hasMeta: Boolean
        get() = _routeMetas.isNotEmpty() || _serviceMetas.isNotEmpty() || _injectMetas.isNotEmpty()

    override val moduleMeta: ModuleMeta
        get() {
            val moduleName = requireNotNull(moduleName())
            return ModuleMeta(
                moduleName,
                String.format(SERVICE_CONTAINER_CLASS_NAME_FORMAT, moduleName.capitalize()),
                _injectMetas.values.toList(),
                _routeMetas,
                _serviceMetas
            )
        }

    override fun addInjectField(
        target: InjectMeta.Target,
        field: InjectMeta.Field
    ) {
        _injectMetas.getOrPut(target.targetClass) { InjectMeta(target) }
            .serviceList.add(field)
    }

    override fun addAutowiredField(
        target: InjectMeta.Target,
        field: InjectMeta.Field
    ) {
        _injectMetas.getOrPut(target.targetClass) { InjectMeta(target) }
            .autowiredList.add(field)
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

internal inline fun String.asModuleName() = "lr" + this.encodeUtf8().sha1().hex()
