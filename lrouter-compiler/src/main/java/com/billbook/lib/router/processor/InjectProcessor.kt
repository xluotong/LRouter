package com.billbook.lib.router.processor

import com.billbook.lib.router.*
import com.billbook.lib.router.ACTIVITY
import com.billbook.lib.router.FRAGMENT
import com.billbook.lib.router.annotation.Autowired
import com.google.auto.service.AutoService
import javax.annotation.processing.ProcessingEnvironment
import javax.annotation.processing.RoundEnvironment
import javax.inject.Inject
import javax.inject.Named
import javax.lang.model.element.Element
import javax.lang.model.element.Modifier
import javax.lang.model.element.TypeElement
import javax.lang.model.util.Elements
import javax.lang.model.util.Types

/**
 * @author xluotong@gmail.com
 */
@AutoService(MetaProcessor::class)
class InjectProcessor : MetaProcessor {

    override val supportedAnnotations: Set<String>
        get() = setOf(Inject::class.java.name, Autowired::class.java.name, Named::class.java.name)

    override fun process(
        processingEnv: ProcessingEnvironment,
        annotations: MutableSet<out TypeElement>,
        roundEnv: RoundEnvironment,
        collector: MetaCollector
    ) {
        val types = processingEnv.typeUtils
        val elements = processingEnv.elementUtils
        val activity = processingEnv.elementUtils.getTypeElement(ACTIVITY).asType()
        val fragment = processingEnv.elementUtils.getTypeElement(FRAGMENT).asType()
        roundEnv.getElementsAnnotatedWith(Autowired::class.java).forEach { element ->
            element.requireAutowired(elements, types)
            val autowired = element.getAnnotation(Autowired::class.java)
            val targetTypeElement = element.enclosingElement as TypeElement
            collector.addAutowiredField(
                InjectMeta.Target(
                    elements.getPackageOf(element).qualifiedName.toString(),
                    targetTypeElement.qualifiedName.toString(),
                    when {
                        types.isSubtype(
                            targetTypeElement.asType(),
                            activity
                        ) -> InjectMeta.TargetType.ACTIVITY
                        types.isSubtype(
                            targetTypeElement.asType(),
                            fragment
                        ) -> InjectMeta.TargetType.FRAGMENT
                        else -> InjectMeta.TargetType.SERVICE
                    }
                ),
                InjectMeta.Field(
                    element.simpleName.toString(),
                    if (autowired.name.isNotEmpty()) autowired.name else element.simpleName.toString(),
                    element.toAndroidTypeKind(types, elements),
                    autowired.required
                )
            )
        }
        roundEnv.getElementsAnnotatedWith(Inject::class.java).forEach { element ->
            if (!element.kind.isField) return@forEach
            element.requireInject(elements, types)
            collector.addInjectField(
                InjectMeta.Target(
                    elements.getPackageOf(element).qualifiedName.toString(),
                    (element.enclosingElement as TypeElement).qualifiedName.toString(),
                    InjectMeta.TargetType.SERVICE
                ),
                InjectMeta.Field(
                    element.simpleName.toString(),
                    element.getAnnotation(Named::class.java)?.value
                        ?: "",
                    element.toAndroidTypeKind(types, elements)
                )
            )
        }
    }
}

private fun Element.requireAutowired(elements: Elements, types: Types) {
    val typeElement = this.enclosingElement as TypeElement
    val activity = elements.getTypeElement(ACTIVITY).asType()
    val fragment = elements.getTypeElement(FRAGMENT).asType()
    errorIf(modifiers.contains(Modifier.PRIVATE)) { "@Autowired target field must not be private!" }
    errorIf(
        !types.isSubtype(typeElement.asType(), activity)
                && !types.isSubtype(
            typeElement.asType(),
            fragment
        )
    ) { "@Autowired target class must be activity or fragment" }
}

private fun Element.requireInject(elements: Elements, types: Types) {
    check(modifiers.contains(Modifier.PRIVATE)) { "@Inject target field must not be private!" }
}