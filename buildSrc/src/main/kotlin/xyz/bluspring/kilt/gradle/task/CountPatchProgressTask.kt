package xyz.bluspring.kilt.gradle.task

import org.gradle.api.DefaultTask
import org.gradle.api.file.ProjectLayout
import org.gradle.api.tasks.TaskAction
import org.gradle.work.DisableCachingByDefault
import java.io.File
import javax.inject.Inject

@DisableCachingByDefault(because = "Not worth caching")
open class CountPatchProgressTask : DefaultTask() {
    @get:Inject
    open lateinit var layout: ProjectLayout

    init {
        group = "kilt"
        description = "Counts the total of patches in Forge, and checks how many Kilt ForgeInjects there are, to check how much is remaining."
        notCompatibleWithConfigurationCache("Must not be cached")
    }

    fun readDir(file: File) : Int {
        val files = file.listFiles()!!
        var count = 0

        files.forEach {
            if (it.isDirectory) {
                count += readDir(it)
            } else {
                count++
            }
        }
        return count
    }

    @TaskAction
    fun run() {
        // Scan Forge patches dir
        val projectDir = layout.projectDirectory
        val forgePatchCount = readDir(projectDir.dir("forge/patches").asFile)
        val kiltInjectCount = readDir(projectDir.dir("src/main/java/xyz/bluspring/kilt/forgeinjects").asFile)

        println(
            "Progress: $kiltInjectCount injects/$forgePatchCount patches (${
                String.format(
                    "%.2f",
                    (kiltInjectCount.toDouble() / forgePatchCount.toDouble()) * 100.0
                )
            }%)"
        )
    }
}