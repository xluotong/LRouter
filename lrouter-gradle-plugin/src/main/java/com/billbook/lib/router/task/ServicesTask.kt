package com.billbook.lib.router.task

import com.android.build.gradle.api.ApplicationVariant
import com.billbook.lib.router.META_DATA_PATH
import com.billbook.lib.router.ModuleMeta
import com.billbook.lib.router.globalGson
import com.google.gson.reflect.TypeToken
import org.gradle.api.Action
import org.gradle.api.DefaultTask
import org.gradle.api.Project
import org.gradle.api.file.FileCollection
import org.gradle.api.provider.Provider
import org.gradle.api.tasks.TaskAction
import org.jetbrains.kotlin.gradle.internal.Kapt3GradleSubplugin
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
        var otherMetas:List<ModuleMeta> = otherLibMeta.singleFile.reader().use { reader ->
            globalGson.fromJson(reader, object : TypeToken<List<ModuleMeta>>(){}.type)
        }
        val currentMeta = metaProvider.get()
        if(currentMeta.exists()){
            otherMetas = otherMetas + globalGson.fromReader<ModuleMeta>(currentMeta.reader())
        }
    }

    class TaskConfigAction(
        private val project: Project,
        private val variant: ApplicationVariant
    ) : Action<ServicesTask> {

        private val outputClassDir
            get() = File(
                project.buildDir,
                "intermediates/lrouter/${variant.name}/registry"
            )
        private val taskName: String
            get() = "generate${variant.name.capitalize()}LRouteRegistry"

        override fun execute(task: ServicesTask) {
            variant.registerPostJavacGeneratedBytecode(
                project.files(outputClassDir).builtBy(taskName)
            )
            task.outputClassDir = outputClassDir
            task.dependsOn(variant.javaCompileProvider)
            task.globalMeta = File(project.buildDir,"outputs/lrouter/${variant.name}/meta.json")
            if (project.plugins.hasPlugin("kotlin-kapt")) {
                task.metaProvider = project.provider {
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

private fun List<ModuleMeta>.generateRegistryClass(dir:File){

}