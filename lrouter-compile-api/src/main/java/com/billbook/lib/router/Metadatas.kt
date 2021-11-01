package com.billbook.lib.router

data class ModuleMeta(
    val name:String,
    val container:String,
    val routeMetas:List<RouteMeta>,
    val serviceMetas:List<ServiceMeta>
)

data class RouteMeta(
    val name:String,
    val desc:String,
    val scheme:String,
    val host:String,
    val path:String,
    val interceptors:List<String>
)

data class ServiceMeta(
    val definition:String,
    val service:String,
    val name: String,
    val desc: String
)