package com.billbook.lib.router.task

import com.android.build.gradle.api.ApplicationVariant
import com.billbook.lib.router.*
import com.google.gson.reflect.TypeToken
import org.gradle.api.Action
import org.gradle.api.DefaultTask
import org.gradle.api.Project
import org.gradle.api.file.FileCollection
import org.gradle.api.provider.Provider
import org.gradle.api.tasks.*
import org.jetbrains.kotlin.gradle.internal.Kapt3GradleSubplugin
import org.objectweb.asm.ClassWriter
import org.objectweb.asm.Opcodes
import org.objectweb.asm.Type
import java.io.File
import java.nio.file.Files
import java.nio.file.Paths

@CacheableTask
open class ModuleGenerateTask : DefaultTask() {

    @get:OutputDirectory
    lateinit var outputClassDir: File
        private set

    @get:OutputFile
    lateinit var globalMeta: File
        private set

    @Classpath
    @get:InputFiles
    lateinit var otherLibMeta: FileCollection
        private set

    @Classpath
    @get:InputFile
    lateinit var metaProvider: Provider<File>
        private set

    @TaskAction
    fun generate() {
        outputClassDir.deleteRecursively()
        Files.createDirectories(Paths.get(outputClassDir.toURI()))
        var otherMetas: List<ModuleMeta> = otherLibMeta.singleFile.reader().use { reader ->
            globalGson.fromJson(reader, object : TypeToken<List<ModuleMeta>>() {}.type)
        }
        val currentMeta = metaProvider.get()
        if (currentMeta.exists()) {
            otherMetas = otherMetas + globalGson.fromReader<ModuleMeta>(currentMeta.reader())
        }
        otherMetas.generateModuleProvider(outputClassDir)
    }

    class ConfigAction(
        private val project: Project,
        private val variant: ApplicationVariant,
        private val otherLibMeta: FileCollection,
    ) : NamedPreConfigureAction<ModuleGenerateTask>() {

        private val outputClassDir
            get() = File(
                project.buildDir,
                "intermediates/lrouter/${variant.name}/generated"
            )

        override val name: String get() = "generate${variant.name.capitalize()}RouteProvider"

        override fun preConfigure(taskName: String) {
            variant.registerPostJavacGeneratedBytecode(
                project.files(outputClassDir).builtBy(taskName)
            )
        }

        override fun execute(task: ModuleGenerateTask) {
            task.group = "lrouter"
            task.otherLibMeta = otherLibMeta
            task.outputClassDir = outputClassDir
            task.dependsOn(variant.javaCompileProvider)
            task.globalMeta = File(project.buildDir, "outputs/lrouter/${variant.name}/app_meta.json")
            task.metaProvider = if (project.plugins.hasPlugin("kotlin-kapt")) {
                project.provider {
                    File(
                        Kapt3GradleSubplugin.getKaptGeneratedClassesDir(project, variant.name),
                        META_DATA_PATH
                    )
                }
            } else {
                variant.javaCompileProvider.map {
                    File(it.destinationDirectory.asFile.orNull, META_DATA_PATH)
                }
            }
        }
    }
}

internal fun String.toPath() = this.replace('.', '/')
private fun String.toInternalName() = this.replace('.', '/')

internal fun File.ensureDir() {
    this?.parentFile?.let { if (!it.exists()) it.mkdirs() }
}

private fun List<ModuleMeta>.generateModuleProvider(dir: File) {
    val file = File(dir, "${MODULES_CLASS.toPath()}.class").also { it.ensureDir() }
    val classWriter = ClassWriter(0)
    classWriter.visit(
        Opcodes.V1_7,
        Opcodes.ACC_PUBLIC or Opcodes.ACC_FINAL or Opcodes.ACC_SUPER or Opcodes.ACC_SYNTHETIC,
        MODULES_CLASS.toInternalName(),
        null,
        Type.getInternalName(Object::class.java),
        null
    )
    val methodVisitor = classWriter.visitMethod(Opcodes.ACC_PRIVATE, "<init>", "()V", null, null)
    methodVisitor.visitCode()
    methodVisitor.visitVarInsn(Opcodes.ALOAD, 0)
    methodVisitor.visitMethodInsn(Opcodes.INVOKESPECIAL, "java/lang/Object", "<init>", "()V", false)
    methodVisitor.visitInsn(Opcodes.RETURN)
    methodVisitor.visitMaxs(1, 1)
    methodVisitor.visitEnd()

    val modulesVisitor = classWriter.visitMethod(
        Opcodes.ACC_PUBLIC or Opcodes.ACC_STATIC,
        "modules",
        "()Ljava/util/List;",
        "()Ljava/util/List<+L${ABSTRACT_MODULE_CONTAINER_CLASS.toInternalName()};>;",
        null
    )
    modulesVisitor.visitCode()
    modulesVisitor.visitTypeInsn(Opcodes.NEW, "java/util/ArrayList")
    modulesVisitor.visitInsn(Opcodes.DUP)
    modulesVisitor.visitLdcInsn(this.size)
    modulesVisitor.visitMethodInsn(
        Opcodes.INVOKESPECIAL,
        "java/util/ArrayList", "<init>", "()V", false
    )
    modulesVisitor.visitVarInsn(Opcodes.ASTORE, 0)
    this.forEach { moduleMeta ->
        modulesVisitor.visitVarInsn(Opcodes.ALOAD, 0)
        modulesVisitor.visitTypeInsn(Opcodes.NEW, moduleMeta.container.toInternalName())
        modulesVisitor.visitInsn(Opcodes.DUP)
        modulesVisitor.visitMethodInsn(
            Opcodes.INVOKESPECIAL,
            moduleMeta.container.toInternalName(),
            "<init>",
            "()V",
            false
        )
        modulesVisitor.visitMethodInsn(
            Opcodes.INVOKEINTERFACE,
            "java/util/List",
            "add",
            "(Ljava/lang/Object;)Z",
            true
        )
        modulesVisitor.visitInsn(Opcodes.POP)
    }
    modulesVisitor.visitVarInsn(Opcodes.ALOAD, 0)
    modulesVisitor.visitInsn(Opcodes.ARETURN)
    methodVisitor.visitMaxs(3, 1)
    methodVisitor.visitEnd()

    classWriter.visitEnd()
    file.writeBytes(classWriter.toByteArray())
}