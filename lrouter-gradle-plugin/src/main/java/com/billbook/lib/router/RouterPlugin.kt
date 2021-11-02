package com.billbook.lib.router

import com.android.build.gradle.AppExtension
import com.android.build.gradle.internal.plugins.BasePlugin
import com.billbook.lib.router.task.CollectMetaTask
import com.billbook.lib.router.task.ServicesTask
import org.gradle.api.Action
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
            with(it.extension) {
                if (this is AppExtension) {
                    this.applicationVariants.configureEach { variant ->
                        val files = project.files()
                        project.tasks.register(
                            CollectMetaTask.ConfigAction(
                                project,
                                variant,
                                files
                            )
                        )
                        project.tasks.register(ServicesTask.ConfigAction(project, variant, files))
                    }
                }
            }
        }
    }
}

internal inline fun <reified T : Task> TaskContainer.register(namedAction: NamedAction<T>) {
    this.register(namedAction.name, T::class.java, namedAction)
}

