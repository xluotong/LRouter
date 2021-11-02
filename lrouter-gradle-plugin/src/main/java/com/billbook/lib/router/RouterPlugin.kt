package com.billbook.lib.router

import com.android.build.api.dsl.ApplicationExtension
import com.android.build.gradle.AppExtension
import com.android.build.gradle.internal.plugins.BasePlugin
import org.gradle.api.Plugin
import org.gradle.api.Project

/**
 * @author xluotong@gmail.com
 */
class RouterPlugin : Plugin<Project> {

    override fun apply(project: Project) {
        project.plugins.withType(BasePlugin::class.java) {
            with(it.extension){
                if(this is AppExtension){
                    this.applicationVariants.configureEach {
                        project.tasks.register()
                    }
                }
            }
        }
    }
}