package com.billbook.lib.router

import com.squareup.javapoet.ClassName
import javax.annotation.processing.ProcessingEnvironment
import javax.tools.Diagnostic

internal const val SERVICE_CONTAINER_CLASS_NAME_FORMAT = "%s_ServiceContainer"

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