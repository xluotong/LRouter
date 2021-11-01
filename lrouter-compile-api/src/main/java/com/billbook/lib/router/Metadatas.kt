package com.billbook.lib.router

const val META_DATA_PATH = "META-INF/lrouters/metadata.json"

data class ModuleMeta(
    val name: String,
    val serviceContainer: String, // Module serviceContainer class name
    val routeMetas: List<RouteMeta>,
    val serviceMetas: List<ServiceMeta>
)

data class RouteMeta(
    val name: String,
    val desc: String,
    val definition: String,
    val scheme: String,
    val host: String,
    val path: String,
    val interceptors: List<String>
)

data class ServiceMeta(
    val definition: String,
    val service: String,
    val name: String,
    val desc: String,
    val singleton: Boolean
)