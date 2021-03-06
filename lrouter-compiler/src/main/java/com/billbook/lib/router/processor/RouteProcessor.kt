package com.billbook.lib.router.processor

import com.billbook.lib.router.*
import com.billbook.lib.router.ACTIVITY
import com.billbook.lib.router.SERVICE
import com.billbook.lib.router.annotation.Route
import com.billbook.lib.router.annotation.Routes
import com.google.auto.service.AutoService
import javax.annotation.processing.ProcessingEnvironment
import javax.annotation.processing.RoundEnvironment
import javax.lang.model.element.Element
import javax.lang.model.element.ElementKind
import javax.lang.model.element.Modifier
import javax.lang.model.element.TypeElement
import javax.lang.model.type.DeclaredType
import javax.lang.model.type.MirroredTypeException
import javax.lang.model.type.MirroredTypesException
import javax.lang.model.type.TypeMirror
import javax.lang.model.util.Elements
import javax.lang.model.util.Types
import kotlin.contracts.contract

/**
 * @author xluotong@gmail.com
 */
@AutoService(MetaProcessor::class)
class RouteProcessor : MetaProcessor {

    override val supportedAnnotations: Set<String>
        get() = setOf(Route::class.java.name, Routes::class.java.name)

    override fun process(
        processingEnv: ProcessingEnvironment,
        annotations: MutableSet<out TypeElement>,
        roundEnv: RoundEnvironment,
        collector: MetaCollector
    ) {
        val activity = processingEnv.elementUtils.getTypeElement(ACTIVITY).asType()
        val service = processingEnv.elementUtils.getTypeElement(SERVICE).asType()
        val fragment = processingEnv.elementUtils.getTypeElement(FRAGMENT).asType()
        val types = processingEnv.typeUtils
        val elements = processingEnv.elementUtils
        roundEnv.getElementsAnnotatedWith(Route::class.java)?.forEach { element ->
            element.require(Route::class.java.canonicalName, elements)
            val annotation = element.getAnnotation(Route::class.java)
            val elementType = element.asType()
            collector.addRoute(
                RouteMeta(
                    "",
                    "",
                    annotation.desc,
                    (element as TypeElement).qualifiedName.toString(),
                    annotation.scheme,
                    annotation.host,
                    annotation.path,
                    when {
                        types.isSubtype(elementType, activity) -> RouteType.ACTIVITY
                        types.isSubtype(elementType, service) -> RouteType.SERVICE
                        types.isSubtype(elementType, fragment) -> RouteType.FRAGMENT
                        else -> RouteType.PROVIDER
                    },
                    annotation.interceptorsType().distinct().map { it.javaClassName },
                )
            )
        }
        roundEnv.getElementsAnnotatedWith(Routes::class.java)?.forEach { element ->
            element.require(Routes::class.java.canonicalName, elements)
            val routes = element.getAnnotation(Routes::class.java)
            val elementType = element.asType()
            val routeType = when {
                types.isSubtype(elementType, activity) -> RouteType.ACTIVITY
                types.isSubtype(elementType, service) -> RouteType.SERVICE
                types.isSubtype(elementType, fragment) -> RouteType.FRAGMENT
                else -> RouteType.PROVIDER
            }
            val shareInterceptors = routes.interceptorsType().map { it.javaClassName }.distinct()
            collector.addRoutes(routes.routes.map { route ->
                RouteMeta(
                    routes.name,
                    routes.desc,
                    route.desc,
                    (element as TypeElement).qualifiedName.toString(),
                    route.scheme,
                    route.host,
                    route.path,
                    routeType,
                    (shareInterceptors + route.interceptorsType()
                        .map { it.javaClassName }).distinct(),
                    routes.launcherType().javaClassName
                )
            })
        }
    }
}

private fun Element.require(target: String, elements: Elements) {
    errorIf(hasAnnotation(Route::class.java) && hasAnnotation(Routes::class.java)) { "Annotation of Routes and Route cannot be used at the same time, please use Services to merge" }
    errorIf(!kind.isClass) { "$target annotation target ${asType()} must be a class." }
    errorIf(isEnum()) { "$target annotation target ${asType()} is a enum class." }
    errorIf(isAbstract()) { "$target annotation target ${asType()} is a abstract class." }
    errorIf(isInnerClass(elements)) { "$target annotation target ${asType()} is a inner class." }
    errorIf(!modifiers.contains(Modifier.PUBLIC)) { "$target annotation target ${asType()} must be a public class." }
}

inline fun errorIf(value: Boolean, lazyMessage: () -> Any): Unit {
    if (value) {
        error(lazyMessage())
    }
}

private fun Routes.launcherType(): TypeMirror {
    return try {
        launcher
        error("Expected to get a MirroredTypeException!")
    } catch (ex: MirroredTypeException) {
        ex.typeMirror
    }
}

private fun Routes.interceptorsType(): List<TypeMirror> {
    return try {
        interceptors
        error("Expected to get a MirroredTypeException!")
    } catch (ex: MirroredTypesException) {
        ex.typeMirrors
    }
}

private fun Route.interceptorsType(): List<TypeMirror> {
    return try {
        interceptors
        error("Expected to get a MirroredTypeException!")
    } catch (ex: MirroredTypesException) {
        ex.typeMirrors
    }
}

internal inline fun Element.isEnum() = this.kind == ElementKind.ENUM

internal inline fun Element.isAbstract() = this.modifiers.contains(Modifier.ABSTRACT)

internal fun Element.isInnerClass(elements: Elements): Boolean {
    if (!this.kind.isClass || this.isEnum()) return false
    if (elements.getPackageOf(this) != this.enclosingElement
        && !this.modifiers.contains(Modifier.STATIC)
    ) {
        return true
    }
    return false
}

internal val TypeMirror.javaClassName: String get() = ((this as DeclaredType).asElement() as TypeElement).qualifiedName.toString()



