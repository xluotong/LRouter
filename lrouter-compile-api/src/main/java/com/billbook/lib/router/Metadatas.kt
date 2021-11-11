package com.billbook.lib.router

const val META_DATA_PATH = "META-INF/lrouter/metadata.json"

data class ModuleMeta(
    val name: String,
    val container: String, // Module container class name
    val injectMeta: List<InjectMeta>,
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
    val interceptors: List<String>,
    val launcher: String = ""
)

data class InjectMeta(
    val target: Target,
    val autowiredList: MutableList<Field> = mutableListOf(),
    val serviceList: MutableList<Field> = mutableListOf(),
) {

    data class Target(
        val packageName: String,
        val targetClass: String,
        val type: TargetType,
    )

    data class Field(
        val name: String,
        val injectName: String,
        val type: String,
        val required: Boolean = false
    )

    enum class TargetType {
        ACTIVITY, FRAGMENT, SERVICE
    }
}

data class ServiceMeta(
    val definition: String,
    val service: String,
    val name: String,
    val desc: String,
    val singleton: Boolean
)