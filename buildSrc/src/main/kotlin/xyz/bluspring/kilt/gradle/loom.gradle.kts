@file:Suppress("UnstableApiUsage")

package xyz.bluspring.kilt.gradle

plugins {
    id("xyz.bluspring.kilt.gradle.java")
    id("fabric-loom")
}

loom {
    mixin {
        showMessageTypes.set(true)
        messages.set(mutableMapOf("ACCESSOR_TARGET_NOT_FOUND" to "disabled"))
    }
}

dependencies {
    minecraft("com.mojang:minecraft:${property("minecraft_version")}")
    mappings(loom.layered {
        officialMojangMappings()
        parchment("org.parchmentmc.data:parchment-${property("parchment_version")}:${property("parchment_release")}@zip")
    })

    modImplementation("net.fabricmc:fabric-loader:${property("loader_version")}")
    modImplementation("net.fabricmc.fabric-api:fabric-api:${property("fabric_version")}")
    modImplementation("dev.architectury:architectury-fabric:${property("architectury_version")}")

    implementation("com.google.code.findbugs:jsr305:3.0.2")
}