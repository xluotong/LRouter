package com.billbook.lib.router

import com.squareup.javapoet.ClassName
import javax.annotation.processing.ProcessingEnvironment
import javax.lang.model.element.Element
import javax.lang.model.element.TypeElement
import javax.lang.model.type.TypeKind
import javax.lang.model.util.Elements
import javax.lang.model.util.Types
import javax.tools.Diagnostic

internal const val SERVICE_CONTAINER_CLASS_NAME_FORMAT = "$PACKAGE_GENERATE"

internal const val ACTIVITY = "android.app.Activity"
internal const val SERVICE = "android.app.Service"
internal const val FRAGMENT = "androidx.fragment.app.Fragment"
internal const val PARCELABLE = "android.os.Parcelable"
internal const val SERIALIZABLE = "java.io.Serializable"

internal inline fun Class<*>.toClassName() = ClassName.get(this)

internal fun String.toClassName(): ClassName {
    val packageIndex = lastIndexOf(".")
    val packageName = if (packageIndex >= 0) {
        substring(0, packageIndex)
    } else {
        ""
    }
    val names = substring(packageIndex + 1).split("$")
    require(names.isNotEmpty() && names.none {
        it.isEmpty()
    }) {
        throw IllegalArgumentException("Illegal class name: $this")
    }
    return ClassName.get(packageName, names[0], *names.subList(1, names.size).toTypedArray())
}

internal fun ProcessingEnvironment.error(message: String) =
    this.messager.printMessage(Diagnostic.Kind.ERROR, message)

internal fun ProcessingEnvironment.warning(message: String) =
    this.messager.printMessage(Diagnostic.Kind.WARNING, message)

internal fun ProcessingEnvironment.info(message: String) =
    this.messager.printMessage(Diagnostic.Kind.NOTE, message)

internal fun Element.toAndroidTypeKind(types: Types, elements: Elements): String {
    val type = asType()
    return when (type.kind) {
        TypeKind.BYTE -> AndroidTypeKind.BYTE
        TypeKind.SHORT -> AndroidTypeKind.SHORT
        TypeKind.INT -> AndroidTypeKind.INT
        TypeKind.LONG -> AndroidTypeKind.LONG
        TypeKind.FLOAT -> AndroidTypeKind.FLOAT
        TypeKind.DOUBLE -> AndroidTypeKind.DOUBLE
        TypeKind.BOOLEAN -> AndroidTypeKind.BOOLEAN
        TypeKind.CHAR -> AndroidTypeKind.CHAR
        else -> {
            when {
                type.toString() == AndroidTypeKind.STRING -> {
                    AndroidTypeKind.STRING
                }
                types.isSubtype(type, elements.getTypeElement(PARCELABLE).asType()) -> {
                    AndroidTypeKind.PARCELABLE
                }
                types.isSubtype(type, elements.getTypeElement(SERIALIZABLE).asType()) -> {
                    AndroidTypeKind.SERIALIZABLE
                }
                else -> ClassName.get(type).toString()
            }
        }
    }
}