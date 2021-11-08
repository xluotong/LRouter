package com.billbook.lib.router

const val META_DATA_PATH = "META-INF/lrouter/metadata.json"

data class ModuleMeta(
    val name: String,
    val container: String, // Module container class name
    val routeMetas: List<RouteMeta>,
    val serviceMetas: List<ServiceMeta>
)

data class RouteMeta(
    val group: String,
    val groupDesc: String,
    val desc: String,
    val definition: String,
    val scheme: String,
    val host: String,
    val path: String,
    val type: RouteType,
    val interceptors: List<String>
)

data class ServiceMeta(
    val definition: String,
    val service: String,
    val name: String,
    val desc: String,
    val singleton: Boolean
)