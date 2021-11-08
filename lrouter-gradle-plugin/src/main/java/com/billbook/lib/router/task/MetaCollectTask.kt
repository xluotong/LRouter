package com.billbook.lib.router.task

import com.android.build.gradle.api.ApplicationVariant
import com.android.build.gradle.api.BaseVariant
import com.android.build.gradle.api.TestVariant
import com.android.build.gradle.internal.publishing.AndroidArtifacts
import com.billbook.lib.router.META_DATA_PATH
import com.billbook.lib.router.ModuleMeta
import com.billbook.lib.router.NamedAction
import com.billbook.lib.router.globalGson
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import org.gradle.api.Action
import org.gradle.api.DefaultTask
import org.gradle.api.Project
import org.gradle.api.file.ConfigurableFileCollection
import org.gradle.api.file.FileCollection
import org.gradle.api.tasks.TaskAction
import java.io.File
import java.io.Reader
import java.util.zip.ZipFile
import kotlin.streams.toList

internal inline fun <reified T> Gson.fromReader(reader: Reader): T = reader.use {
    return this.fromJson(reader, object : TypeToken<T>() {}.type)
}

class CollectMetaTask : DefaultTask() {

    private lateinit var outputFile: File
    private lateinit var inputFileCollection: FileCollection

    @TaskAction
    fun collect() {
        val moduleMetas = inputFileCollection.files
            .parallelStream()
            .filter { it.exists() }
            .map {
                if (it.endsWith(".jar")) {
                    ZipFile(it).use { file ->
                        file.getEntry(META_DATA_PATH)?.let { entry ->
                            globalGson.fromReader<List<ModuleMeta>>(
                                file.getInputStream(entry).reader()
                            )
                        }
                    }
                } else {
                    val file = File(it, META_DATA_PATH.replace('/', File.separatorChar))
                    if (file.exists()) {
                        globalGson.fromReader<List<ModuleMeta>>(file.reader())
                    } else null
                }
            }
            .filter { it != null }
            .toList() as List<ModuleMeta>
        outputFile.bufferedWriter().use { globalGson.toJson(moduleMetas, it) }
    }

    class ConfigAction(
        private val project: Project,
        private val variant: ApplicationVariant,
        private val fileCollection: ConfigurableFileCollection
    ) : NamedAction<CollectMetaTask> {

        private val outputFile: File
            get() = File(
                project.buildDir,
                "intermediates/lrouter/${variant.name}/other_meta.json"
            )

        override val name: String get() = "collect${variant.name.capitalize()}LRouteMeta"

        override fun execute(task: CollectMetaTask) {
            fileCollection.builtBy(name)
                .from(outputFile)
            task.inputFileCollection = project.files()
                .fromVariant(variant)
                .apply {
                    if (variant is TestVariant) {
                        fromVariant(variant.testedVariant)
                    }
                }
        }
    }
}

internal fun ConfigurableFileCollection.fromVariant(variant: BaseVariant): ConfigurableFileCollection {
    from(variant.runtimeConfiguration
        .incoming
        .artifactView {
            attributes {
                attribute(
                    AndroidArtifacts.ARTIFACT_TYPE,
                    AndroidArtifacts.ArtifactType.JAVA_RES.type
                )
            }
        }
        .files)
    return this
}