@file:Suppress("UnstableApiUsage", "LocalVariableName")

enableFeaturePreview("STABLE_CONFIGURATION_CACHE")

pluginManagement {
    repositories {
        maven("https://maven.fabricmc.net/") {
            name = "Fabric"
        }
        maven("https://dl.bintray.com/brambolt/public")
        mavenCentral()
        gradlePluginPortal()
    }

    val fabric_kotlin_version: String by settings
    plugins {
        id("org.jetbrains.kotlin.jvm") version
                fabric_kotlin_version
                    .split("+kotlin.")[1] // Grabs the sentence after `+kotlin.`
                    .split("+")[0] // Ensures sentences like `+build.1` are ignored
    }
}

rootProject.name = extra["project_name"] as String

val moduleRoot = rootDir.resolve("modules")
moduleRoot.listFiles { f: File -> f.isDirectory }?.forEach { dir ->
    include(":modules:${dir.name}")
    project(":modules:${dir.name}").apply {
        if (!dir.resolve("default.gradle.kts").exists()) {
            buildFileName = "../default.gradle.kts"
        }
    }
}