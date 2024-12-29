package xyz.bluspring.kilt.gradle.task

import org.gradle.api.DefaultTask
import org.gradle.api.file.ProjectLayout
import org.gradle.api.tasks.TaskAction
import org.gradle.work.DisableCachingByDefault
import java.io.File
import java.security.MessageDigest
import javax.inject.Inject

@DisableCachingByDefault(because = "Must not be cached")
open class TagPatchesTask : DefaultTask() {
    @get:Inject
    open lateinit var layout: ProjectLayout

    init {
        group = "kilt"
        description = "Tags the Kilt ForgeInjects with their currently tracked patch hash to ensure they are all up to date."
        notCompatibleWithConfigurationCache("Must not be cached")
    }

    fun walk(file: File, action: (File) -> Unit) {
        val files = file.listFiles()!!
        files.forEach {
            if (it.isDirectory) {
                walk(it, action)
            } else {
                action(it)
            }
        }
    }

    @TaskAction
    fun run() {
        val md = MessageDigest.getInstance("SHA1")

        walk(layout.projectDirectory.dir("src/main/java/xyz/bluspring/kilt/forgeinjects").asFile) {
            val startDir = it.absolutePath.replace("\\", "/").replaceBefore("forgeinjects/", "").replace("forgeinjects/", "")
            val patchDir = if (startDir.startsWith("blaze3d") || startDir.startsWith("math")) "com/mojang/${startDir.replace("Inject.java", ".java.patch")}"
            else "net/minecraft/${startDir.replace("Inject.java", ".java.patch")}"

            val patchFile = layout.projectDirectory.file("forge/patches/minecraft/$patchDir").asFile
            if (!patchFile.exists()) {
                println("!! WARNING !! Inject $startDir no longer has an associated patch file!")
                return@walk
            }

            val patchHash = md.digest(patchFile.readBytes())
                .joinToString("") { "%02x".format(it) }

            val data = it.readLines().toMutableList()
            if (!data[0].startsWith("// TRACKED HASH: ")) {
                data.add(0, "// TRACKED HASH: $patchHash")
                it.writeText(data.joinToString("\r\n"))
            } else {
                val oldHash = data[0].removePrefix("// TRACKED HASH: ")

                if (oldHash != patchHash) {
                    println("Inject $startDir is outdated! (patch: $patchHash, inject: $oldHash) Updating hash...")
                    data[0] = "// TRACKED HASH: $patchHash"
                    it.writeText(data.joinToString("\r\n"))
                }
            }
        }
    }
}