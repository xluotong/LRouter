package com.billbook.lib.router

import com.android.build.gradle.AppExtension
import com.android.build.gradle.BaseExtension
import com.android.build.gradle.BasePlugin
import com.billbook.lib.router.task.CollectMetaTask
import com.billbook.lib.router.task.ModuleGenerateTask
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.Task
import org.gradle.api.tasks.TaskContainer

/**
 * @author xluotong@gmail.com
 */
class RouterPlugin : Plugin<Project> {

    override fun apply(project: Project) {
        project.plugins.withType(BasePlugin::class.java) {
            val extension: BaseExtension = project.extensions.getByName("android") as BaseExtension
            if (extension is AppExtension) {
                extension.applicationVariants.configureEach {
                    val files = project.files()
                    project.tasks.register(
                        CollectMetaTask.ConfigAction(
                            project,
                            this,
                            files
                        )
                    )
                    project.tasks.register(ModuleGenerateTask.ConfigAction(project, this, files))
                }
            }
        }
    }
}

internal inline fun <reified T : Task> TaskContainer.register(namedAction: NamedPreConfigureAction<T>) {
    this.register(namedAction.name, T::class.java, namedAction).apply {
        namedAction.preConfigure(this.name)
    }
}

