package com.billbook.lib.router

import org.gradle.api.Plugin
import org.gradle.api.Project

/**
 * @author xluotong@gmail.com
 */
class RouterPlugin : Plugin<Project> {

    override fun apply(project: Project) {
        project.extensions.getByType<AppBa>()
    }
}