package xyz.bluspring.kilt.gradle.task

import org.gradle.api.DefaultTask
import org.gradle.api.file.ProjectLayout
import org.gradle.api.model.ObjectFactory
import org.gradle.api.provider.Property
import org.gradle.api.provider.ProviderFactory
import org.gradle.api.tasks.Input
import org.gradle.api.tasks.InputFile
import org.gradle.api.tasks.OutputFile
import org.gradle.api.tasks.TaskAction
import xyz.bluspring.kilt.gradle.util.AccessTransformerRemapper
import java.io.File
import javax.inject.Inject

open class TransformerToWidenerTask : DefaultTask() {
    @get:Inject
    open lateinit var layout: ProjectLayout
    @get:Inject
    open lateinit var objects: ObjectFactory
    @get:Inject
    open lateinit var provider: ProviderFactory

    @get:Input
    open val minecraftVersion: Property<String> =
        objects.property(String::class.java).value(provider.gradleProperty("minecraft_version"))
    @get:InputFile
    open val transformerFile: Property<File> =
        objects.property(File::class.java)
    @get:OutputFile
    open val widenerFile: Property<File> =
        objects.property(File::class.java)

    init {
        group = "kilt"
        description = "Converts the Forge AccessTransformer to a Fabric AccessWidener file."
    }

    @TaskAction
    fun run() {
        AccessTransformerRemapper.convertTransformerToWidener(
            transformerFile.get().readText(),
            widenerFile.get(),
            minecraftVersion.get(),
            layout.buildDirectory.asFile.get()
        )
    }
}