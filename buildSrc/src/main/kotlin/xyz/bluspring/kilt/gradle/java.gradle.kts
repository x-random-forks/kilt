package xyz.bluspring.kilt.gradle

import xyz.bluspring.kilt.gradle.util.includeGroup
import xyz.bluspring.kilt.gradle.util.maven

plugins {
    `java-library`
}

val targetJavaVersion = "17"

repositories {
    mavenLocal()
    mavenCentral()

    maven("MinecraftForge Maven", "https://maven.minecraftforge.net/")
    maven("FabricMC", "https://maven.fabricmc.net/")
    maven("Architectury", "https://maven.architectury.dev")
    maven("ParchmentMC", "https://maven.parchmentmc.org")
    maven("devOS Maven", "https://mvn.devos.one/releases/")
    maven("devOS Maven (Snapshots)", "https://mvn.devos.one/snapshots/")
    maven("Cafeteria Dev", "https://maven.cafeteria.dev/releases/")
    maven("Fuzs Mod Resources", "https://raw.githubusercontent.com/Fuzss/modresources/main/maven/")
    maven("JamiesWhiteShirt Dev", "https://maven.jamieswhiteshirt.com/libs-release").includeGroup("com.jamieswhiteshirt")
    maven("JitPack", "https://jitpack.io/")
    flatDir { dir("libs") }

    // Testing mod sources
    maven("TerraformersMC", "https://maven.terraformersmc.com/")
    maven("Su5ed", "https://maven.su5ed.dev/releases")
    maven("Modrinth", "https://api.modrinth.com/maven").includeGroup("maven.modrinth")
    maven("CurseMaven", "https://cursemaven.com").includeGroup("curse.maven")
}

java {
    withJavadocJar()
    withSourcesJar()

    val javaVersion = JavaVersion.toVersion(targetJavaVersion)
    if (JavaVersion.current() < javaVersion) {
        toolchain.languageVersion.set(JavaLanguageVersion.of(targetJavaVersion))
    } else {
        sourceCompatibility = javaVersion
        targetCompatibility = javaVersion
    }
}

tasks {
    withType<org.jetbrains.kotlin.gradle.tasks.KotlinCompile> {
        compilerOptions {
            jvmTarget.set(JvmTarget.fromTarget(targetJavaVersion))
        }
    }

    jar {
        from("LICENSE") {
            rename { "${it}_${archiveBaseName.get()}" }
        }
    }

    named<Jar>("sourcesJar") {
        duplicatesStrategy = DuplicatesStrategy.WARN
    }
}

//publishing {
//    publications {
//        create<MavenPublication>("mavenJava") {
//            artifact(remapJar) {
//                builtBy(remapJar)
//            }
//            artifact(kotlinSourcesJar) {
//                builtBy(remapSourcesJar)
//            }
//        }
//    }
//
//    repositories {
//        mavenLocal()
//    }
//}
