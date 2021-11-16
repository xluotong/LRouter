package com.billbook.lib.router.processor

import com.billbook.lib.router.MetaCollector
import com.billbook.lib.router.MetaProcessor
import com.billbook.lib.router.ServiceMeta
import com.billbook.lib.router.annotation.Route
import com.billbook.lib.router.annotation.Routes
import com.billbook.lib.router.annotation.Service
import com.billbook.lib.router.annotation.Services
import com.google.auto.service.AutoService
import javax.annotation.processing.ProcessingEnvironment
import javax.annotation.processing.RoundEnvironment
import javax.inject.Singleton
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

/**
 * @author xluotong@gmail.com
 */
@AutoService(MetaProcessor::class)
class ServiceProcessor : MetaProcessor {

    override val supportedAnnotations: Set<String>
        get() = setOf(Services::class.java.name, Service::class.java.name)

    override fun process(
        processingEnv: ProcessingEnvironment,
        annotations: MutableSet<out TypeElement>,
        roundEnv: RoundEnvironment,
        collector: MetaCollector
    ) {
        roundEnv.getElementsAnnotatedWith(Service::class.java)?.forEach { element ->
            element.require("@Service", processingEnv.elementUtils, processingEnv.typeUtils)
            collector.addService(
                element.getAnnotation(Service::class.java).toServiceMeta(element as TypeElement)
            )
        }
        roundEnv.getElementsAnnotatedWith(Services::class.java)?.forEach { element ->
            element.require("@Services", processingEnv.elementUtils, processingEnv.typeUtils)
            collector.addServices(element.getAnnotation(Services::class.java).services.map {
                it.toServiceMeta(element as TypeElement)
            })
            collector.addServices(element.getAnnotation(Services::class.java).serviceType().map {
                it.toServiceMeta(element as TypeElement)
            })
        }
    }
}

internal fun <T : Annotation> Element.hasAnnotation(clazz: Class<T>) =
    this.getAnnotation(clazz) != null

private fun Service.toServiceMeta(typeElement: TypeElement): ServiceMeta {
    return ServiceMeta(
        serviceType().javaClassName,
        typeElement.qualifiedName.toString(),
        name,
        desc,
        typeElement.hasAnnotation(Singleton::class.java)
    )
}

private fun TypeMirror.toServiceMeta(typeElement: TypeElement): ServiceMeta {
    return ServiceMeta(
        this.javaClassName,
        typeElement.qualifiedName.toString(),
        "",
        "",
        typeElement.hasAnnotation(Singleton::class.java)
    )
}

private fun Element.require(target: String, elements: Elements, types: Types) {
    check(hasAnnotation(Services::class.java) && hasAnnotation(Service::class.java)) { "Annotation of Services and Service cannot be used at the same time, please use Services to merge" }
    check(!kind.isClass) { "$target annotation target ${asType()} must be a class." }
    check(isEnum()) { "$target annotation target ${asType()} is a enum class." }
    check(isAbstract()) { "$target annotation target ${asType()} is a abstract class." }
    check(isInnerClass(elements)) { "$target annotation target ${asType()} is a inner class." }
    check(!modifiers.contains(Modifier.PUBLIC)) { "$target annotation target ${asType()} must be a public class." }
    getAnnotation(Services::class.java)?.serviceType()?.find {
        !types.isAssignable(asType(), it)
    }?.let { error("${this.simpleName} does not implement ${it.javaClassName}！") }
    getAnnotation(Services::class.java)?.services?.map { it.serviceType() }?.find {
        !types.isAssignable(asType(), it)
    }?.let { error("${this.simpleName} does not implement ${it.javaClassName}！") }
    getAnnotation(Service::class.java)?.serviceType()?.takeIf {
        !types.isAssignable(asType(), it)
    }?.let { error("${this.simpleName} does not implement ${it.javaClassName}！") }
}

private fun Service.serviceType(): TypeMirror {
    return try {
        value
        error("Expected to get a MirroredTypeException!")
    } catch (ex: MirroredTypeException) {
        ex.typeMirror
    }
}

private fun Services.serviceType(): List<TypeMirror> {
    return try {
        value
        error("Expected to get a MirroredTypeException!")
    } catch (ex: MirroredTypesException) {
        ex.typeMirrors
    }
}


