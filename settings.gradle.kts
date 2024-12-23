@file:Suppress("UnstableApiUsage")

import org.gradle.kotlin.dsl.dir
import org.gradle.kotlin.dsl.flatDir
import org.gradle.kotlin.dsl.mavenCentral


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

plugins {
    id("org.gradle.toolchains.foojay-resolver-convention") version "0.9.0"
}

rootProject.name = "Kilt"
