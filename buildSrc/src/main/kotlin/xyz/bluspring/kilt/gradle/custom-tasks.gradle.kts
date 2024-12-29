package xyz.bluspring.kilt.gradle

import xyz.bluspring.kilt.gradle.task.*

tasks {
    register<CountPatchProgressTask>("countPatchProgress")
    register<TagPatchesTask>("tagPatches")
    register<SetupDevEnvironmentTask>("setupDevEnvironment")

    register<TransformerToWidenerTask>("transformerToWidener") {
        transformerFile.set(file("forge/src/main/resources/META-INF/accesstransformer.cfg"))
        widenerFile.set(file("src/main/resources/kilt.accesswidener"))
    }
}