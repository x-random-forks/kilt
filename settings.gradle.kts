@file:Suppress("UnstableApiUsage")

enableFeaturePreview("STABLE_CONFIGURATION_CACHE")
enableFeaturePreview("TYPESAFE_PROJECT_ACCESSORS")

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

dependencyResolutionManagement {
    repositories {
        mavenCentral()
        mavenLocal()

        maven("https://mvn.devos.one/releases/") {
            name = "devOS Maven"
        }

        maven("https://mvn.devos.one/snapshots/") {
            name = "devOS Maven (Snapshots)"
        }

        maven("https://jitpack.io/") {
            name = "JitPack"
        }

        maven("https://maven.cafeteria.dev/releases/") {
            name = "Cafeteria Dev"
        }

        maven("https://maven.jamieswhiteshirt.com/libs-release") {
            name = "JamiesWhiteShirt Dev"
            content {
                includeGroup("com.jamieswhiteshirt")
            }
        }

        maven("https://raw.githubusercontent.com/Fuzss/modresources/main/maven/") {
            name = "Fuzs Mod Resources"
        }

        maven("https://maven.minecraftforge.net/") {
            name = "MinecraftForge Maven"
        }

        maven("https://maven.architectury.dev") {
            name = "Architectury"
        }

        maven("https://maven.parchmentmc.org") {
            name = "ParchmentMC"
        }

        flatDir {
            dir("libs")
        }

        // Testing mod sources
        maven("https://api.modrinth.com/maven") {
            name = "Modrinth"
            content {
                includeGroup("maven.modrinth")
            }
        }

        maven("https://cursemaven.com") {
            name = "CurseMaven"
            content {
                includeGroup("curse.maven")
            }
        }

        maven("https://maven.terraformersmc.com/") {
            name = "TerraformersMC"
        }

        maven("https://maven.su5ed.dev/releases") {
            name = "Su5ed"
        }
    }
}

rootProject.name = "Kilt"

rootDir.listFiles { f: File -> f.isDirectory && f.name.startsWith("kilt-") }?.forEach { dir ->
    include(dir.name)
}