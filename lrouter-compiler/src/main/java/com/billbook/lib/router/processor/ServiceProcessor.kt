package com.billbook.lib.router.processor

import com.billbook.lib.router.MetaCollector
import com.billbook.lib.router.MetaProcessor
import com.billbook.lib.router.ServiceMeta
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
import kotlin.reflect.KClass

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
            element.requireElement(processingEnv.typeUtils)
            collector.addService(
                element.getAnnotation(Service::class.java).toServiceMeta(element as TypeElement)
            )
        }
        roundEnv.getElementsAnnotatedWith(Services::class.java)?.forEach { element ->
            element.requireElement(processingEnv.typeUtils)
            collector.addServices(element.getAnnotation(Services::class.java).services.map {
                it.toServiceMeta(
                    element as TypeElement
                )
            })
            collector.addServices(element.getAnnotation(Services::class.java).serviceType().map {
                it.toServiceMeta(
                    element as TypeElement
                )
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

private fun Element.requireElement(types: Types): Element {
    if (hasAnnotation(Services::class.java) && hasAnnotation(Service::class.java)) {
        error("Services and Services cannot be used at the same time, please use Services to merge")
    }
    if (kind.isInterface) error("Services annotation target is a interface or an annotation type.！")
    if (kind == ElementKind.ENUM) error("Services annotation target is a enum class！")
    if (modifiers.contains(Modifier.ABSTRACT)) error("Services annotation target is a abstract class！")
    getAnnotation(Services::class.java)?.serviceType()?.find {
        !types.isAssignable(asType(), it)
    }?.let { error("${this.simpleName} does not implement ${it.javaClassName}！") }
    getAnnotation(Services::class.java)?.services?.map { it.serviceType() }?.find {
        !types.isAssignable(asType(), it)
    }?.let { error("${this.simpleName} does not implement ${it.javaClassName}！") }
    getAnnotation(Service::class.java)?.serviceType()?.takeIf {
        !types.isAssignable(asType(), it)
    }?.let { error("${this.simpleName} does not implement ${it.javaClassName}！") }
    // TODO 判断是不是内部类
    return this
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

internal val TypeMirror.javaClassName: String get() = ((this as DeclaredType).asElement() as TypeElement).qualifiedName.toString()


