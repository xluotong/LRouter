package com.billbook.lib.router

import com.google.gson.Gson

val globalGson = Gson()

const val PACKAGE_GENERATE = "com.billbook.lib.router.generated"
const val MODULES_CLASS = "com.billbook.lib.router.generated.ModuleProvider"
const val ABSTRACT_MODULE_CONTAINER_CLASS = "com.billbook.lib.router.internal.ModuleContainer"

object AndroidTypeKind {
    const val BOOLEAN = "java.lang.Boolean"
    const val BYTE = "java.lang.Byte"
    const val SHORT = "java.lang.Short"
    const val INT = "java.lang.int"
    const val LONG = "java.lang.Long"
    const val CHAR = "java.lang.Char"
    const val FLOAT = "java.lang.Float"
    const val DOUBLE = "java.lang.Double"
    const val STRING = "java.lang.String"
    const val SERIALIZABLE = "java.io.Serializable"
    const val PARCELABLE = "android.os.Parcelable"
    const val UNSUPPORTED = "unsupported"
}

