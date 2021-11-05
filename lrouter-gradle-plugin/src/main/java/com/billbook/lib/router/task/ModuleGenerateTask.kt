package com.billbook.lib.router.task

import com.android.build.gradle.api.ApplicationVariant
import com.billbook.lib.router.*
import com.google.gson.reflect.TypeToken
import org.gradle.api.Action
import org.gradle.api.DefaultTask
import org.gradle.api.Project
import org.gradle.api.file.FileCollection
import org.gradle.api.provider.Provider
import org.gradle.api.tasks.TaskAction
import org.jetbrains.kotlin.gradle.internal.Kapt3GradleSubplugin
import org.objectweb.asm.ClassWriter
import org.objectweb.asm.Opcodes
import java.io.File
import java.nio.file.Files
import java.nio.file.Paths

class ServicesTask : DefaultTask() {

    private lateinit var outputClassDir: File
    private lateinit var otherLibMeta: FileCollection
    private lateinit var metaProvider: Provider<File>
    private lateinit var globalMeta: File

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
    ) : NamedAction<ServicesTask> {

        private val outputClassDir
            get() = File(
                project.buildDir,
                "intermediates/lrouter/${variant.name}/registry"
            )
        override val name: String get() = "generate${variant.name.capitalize()}LRouteRegistry"

        override fun execute(task: ServicesTask) {
            variant.registerPostJavacGeneratedBytecode(
                project.files(outputClassDir).builtBy(name)
            )
            task.otherLibMeta = otherLibMeta
            task.outputClassDir = outputClassDir
            task.dependsOn(variant.javaCompileProvider)
            task.globalMeta = File(project.buildDir, "outputs/lrouter/${variant.name}/meta.json")
            if (project.plugins.hasPlugin("kotlin-kapt")) {
                task.metaProvider = project.provider {
                    File(
                        Kapt3GradleSubplugin.getKaptGeneratedClassesDir(project, variant.name),
                        META_DATA_PATH
                    )
                }
            } else {
                task.metaProvider = variant.javaCompileProvider.map {
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
    val file = File(dir, MODULES_CLASS.toPath()).also { it.ensureDir() }
    val classWriter = ClassWriter(0)
    classWriter.visit(
        Opcodes.V1_7,
        Opcodes.ACC_PUBLIC or Opcodes.ACC_FINAL or Opcodes.ACC_SUPER,
        MODULES_CLASS.toInternalName(),
        null,
        "java/lang/object",
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