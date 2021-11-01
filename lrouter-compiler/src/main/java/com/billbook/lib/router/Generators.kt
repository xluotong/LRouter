package com.billbook.lib.router

import com.billbook.lib.router.internal.CacheIn
import com.billbook.lib.router.internal.ServiceContainer
import com.billbook.lib.router.internal.ServiceInfo
import com.billbook.lib.router.internal.ServiceRegistry
import com.squareup.javapoet.*
import javax.annotation.Generated
import javax.annotation.processing.Filer
import javax.lang.model.element.Modifier
import javax.tools.StandardLocation

/**
 * @author xluotong@gmail.com
 */
private fun ModuleMeta.writeServiceContainerClassTo(filer: Filer) {
    val moduleMeta = this
    JavaFile.builder(
        PACKAGE_GENERATE,
        TypeSpec.classBuilder(moduleMeta.serviceContainer)
            .addJavadoc("Generated by lrouter, please do not edit it!")
            .addAnnotation(
                AnnotationSpec.builder(Generated::class.java)
                    .addMember("value", "\$S", "LRouterGeneartor")
                    .addMember("comments", "\$S", "Generated by lrouter, please do not edit it!")
                    .build()
            )
            .addModifiers(Modifier.PUBLIC, Modifier.FINAL)
            .superclass(ClassName.get(ServiceContainer::class.java))
            .addMethod(
                MethodSpec.methodBuilder("onRegister")
                    .addModifiers(Modifier.PUBLIC)
                    .addAnnotation(Override::class.java)
                    .addParameter(ClassName.get(ServiceRegistry::class.java), "registry")
                    .addCode(
                        CodeBlock.builder().apply {
                            moduleMeta.serviceMetas.forEach { serviceMeta ->
                                this.addStatement(
                                    "registry.register(new \$T(\$S,\$T.class,\$T.class,\$S,\$S,\$T.\$L))",
                                    ClassName.get(ServiceInfo::class.java),
                                    moduleMeta.name,
                                    serviceMeta.definition.toClassName(),
                                    serviceMeta.service.toClassName(),
                                    serviceMeta.name,
                                    serviceMeta.desc,
                                    ClassName.get(CacheIn::class.java),
                                    if (serviceMeta.singleton) CacheIn.SINGLETON else CacheIn.UNDEFINED
                                )
                            }
                        }.build()
                    )
                    .returns(TypeName.VOID)
                    .build()
            )
            .build()
    ).build().writeTo(filer)
}

internal fun ModuleMeta.writeTo(filer: Filer) {
    this.writeServiceContainerClassTo(filer)
    this.writeMetaInfoTo(filer)
}

private fun ModuleMeta.writeMetaInfoTo(filer: Filer) {
    filer.createResource(StandardLocation.CLASS_OUTPUT, "", META_DATA_PATH).openWriter()
        .use { it.write(globalGson.toJson(this)) }
}