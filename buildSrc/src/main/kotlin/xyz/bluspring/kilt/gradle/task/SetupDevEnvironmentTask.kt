package xyz.bluspring.kilt.gradle.task

import org.gradle.api.DefaultTask
import org.gradle.api.file.FileSystemOperations
import org.gradle.api.file.ProjectLayout
import org.gradle.api.tasks.TaskAction
import javax.inject.Inject

open class SetupDevEnvironmentTask : DefaultTask() {
    @get:Inject
    open lateinit var layout: ProjectLayout
    @get:Inject
    open lateinit var fs: FileSystemOperations

    init {
        group = "kilt"
        description = "Sets up the development environment for Kilt."
        notCompatibleWithConfigurationCache("Must not be cached")
    }

    @TaskAction
    fun run() {
        val configDir = layout.projectDirectory.dir("run/config")
        configDir.asFile.mkdirs()

        val loaderDepsFile = configDir.file("fabric_loader_dependencies.json")
        val templateFile = layout.projectDirectory.file("gradle/loader_dep_overrides.json")
        fs.copy {
            from(templateFile)
            into(loaderDepsFile)
        }
    }
}