package com.billbook.lib.router

import com.google.auto.service.AutoService
import java.util.*
import javax.annotation.processing.AbstractProcessor
import javax.annotation.processing.ProcessingEnvironment
import javax.annotation.processing.Processor
import javax.annotation.processing.RoundEnvironment
import javax.lang.model.SourceVersion
import javax.lang.model.element.TypeElement

interface MetaProcessor {
    val supportedAnnotations: Set<String>

    fun process(
        processingEnv: ProcessingEnvironment,
        annotations: MutableSet<out TypeElement>,
        roundEnv: RoundEnvironment,
        collector: MetaCollector
    )
}

@AutoService(Processor::class)
class RouterProcessor : AbstractProcessor() {

    private val mMetaProcessors: List<MetaProcessor> by lazy {
        ServiceLoader.load(MetaProcessor::class.java, MetaProcessor::class.java.classLoader)
            .toList()
    }
    private val mMetaCollector by lazy { MetaCollectorImpl() }

    override fun getSupportedAnnotationTypes(): MutableSet<String> {
        return mMetaProcessors.flatMapTo(mutableSetOf()) {
            it.supportedAnnotations
        }
    }

    override fun getSupportedSourceVersion(): SourceVersion = SourceVersion.latestSupported()

    override fun process(
        annotations: MutableSet<out TypeElement>,
        roundEnv: RoundEnvironment
    ): Boolean {
        if (roundEnv.processingOver()) {
            if (mMetaCollector.hasMeta) mMetaCollector.moduleMeta.writeTo(processingEnv.filer)
        } else {
            mMetaProcessors.forEach {
                it.process(
                    processingEnv,
                    annotations,
                    roundEnv,
                    mMetaCollector
                )
            }
        }
        return false
    }
}