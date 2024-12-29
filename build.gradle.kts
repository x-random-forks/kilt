@file:Suppress("LocalVariableName", "UnstableApiUsage")

import org.ajoberstar.grgit.Grgit

plugins {
    kotlin("jvm")
    id ("xyz.bluspring.kilt.gradle.loom")
    id ("org.ajoberstar.grgit") version "5.0.0"
    id ("xyz.bluspring.kilt.gradle.custom-tasks")
}

group = property("maven_group")!!
version = "${property("mod_version")}+mc${property("minecraft_version")}${getVersionMetadata()}"

sourceSets {
    getByName("main") {
        java.srcDir("src/main/java")
        java.srcDir("src/main/kotlin")
        java.srcDir("forge/src/main/java")

        resources.srcDir("forge/src/generated/resources")
        resources.srcDir("forge/src/main/resources")
    }
}

loom {
    accessWidenerPath.set(file("src/main/resources/kilt.accesswidener"))
}

tasks {
    withType<org.jetbrains.kotlin.gradle.tasks.KotlinCompile> {
    }
}

dependencies {
    //! Minecraft, Mappings, Fabric Loader/API, and most dependencies are handled in the `loom` custom plugin

    // Just because I like Kotlin more than Java
    modRuntimeOnly("net.fabricmc:fabric-language-kotlin:${property("fabric_kotlin_version")}")

    // we require Indium due to us using Fabric Rendering API stuff.
    // let's tell the users that too.
    include("me.luligabi:NoIndium:${property("no_indium_version")}") {
        exclude("net.fabricmc", "fabric-loader")
    }

    // Kilt modules
    rootDir.resolve("modules").listFiles { f: File -> f.isDirectory }?.forEach {
        include(implementation(project(":modules:${it.name}"))!!)
    }

    // Forge Reimplementations
    val portingLibs = listOf("accessors", "asm", "attributes", "base", "blocks", "brewing", "chunk_loading", "client_events", "common", "core", "data", "entity", "extensions", "fluids", "gametest", "gui_utils", "items", "lazy_registration", "level_events", "loot", "mixin_extensions", "model_builders", "model_generators", "model_loader", "model_materials", "models", "networking", "obj_loader", "recipe_book_categories", "registries", "tags", "tool_actions", "transfer", "utility")
    portingLibs.forEach { lib ->
        include(modImplementation("io.github.fabricators_of_create.Porting-Lib:$lib:${property("porting_lib_version")}")!!)
    }

    // Cursed Fabric/Mixin stuff
    include(implementation("com.github.FabricCompatibilityLayers:CursedMixinExtensions:${property("cursedmixinextensions_version")}")!!)
    include(modImplementation("com.github.Chocohead:Fabric-ASM:v${property("fabric_asm_version")}")!!)
    include(implementation(annotationProcessor("com.github.bawnorton.mixinsquared:mixinsquared-fabric:${property("mixin_squared_version")}")!!)!!)
    include(modImplementation("de.florianmichael:AsmFabricLoader:${property("asmfabricloader_version")}")!!)

    // TODO: remove this when 0.5 is mainlined into Fabric
    include(implementation(annotationProcessor("io.github.llamalad7:mixinextras-fabric:${property("mixinextras_version")}")!!)!!)

    modImplementation("fuzs.forgeconfigapiport:forgeconfigapiport-fabric:${property("forgeconfigapiport_version")}")
    //include(modImplementation("io.github.tropheusj:serialization-hooks:${property("serialization_hooks_version")}")!!)
    include(modImplementation("com.jamieswhiteshirt:reach-entity-attributes:${property("reach_entity_attributes_version")}")!!)
    include(implementation("xyz.bluspring.kiltmc:MixinConstraints:${property("mixinconstraints_version")}") {
        exclude("org.spongepowered", "mixin")
    })

    // Forge stuff
    implementation(include("xyz.bluspring:eventbus:${property("eventbus_version")}") {
        exclude("cpw.mods", "modlauncher")
        exclude("net.minecraftforge", "modlauncher")
        exclude("net.minecraftforge", "securemodules")
    })
    implementation(include("net.minecraftforge:forgespi:${property("forgespi_version")}") {
        exclude("cpw.mods", "modlauncher")
        exclude("net.minecraftforge", "modlauncher")
        exclude("net.minecraftforge", "securemodules")
    })
    implementation(include("org.apache.maven:maven-artifact:3.8.5")!!)
    implementation(include("cpw.mods:securejarhandler:${property("securejarhandler_version")}")!!)
    implementation(include("net.jodah:typetools:0.8.3")!!)
    implementation(include("net.minecraftforge:unsafe:0.2.+")!!)
    implementation(include("net.minecraftforge:mergetool-api:1.0")!!)
    implementation(include("org.jline:jline-reader:3.12.+")!!)
    implementation(include("net.minecrell:terminalconsoleappender:1.3.0")!!)
    implementation(include("org.openjdk.nashorn:nashorn-core:${property("nashorn_version")}")!!) // for CoreMods

    // Remapping SRG to Intermediary
    implementation(include("net.minecraftforge:srgutils:0.4.13")!!)
    implementation(include("net.fabricmc:tiny-mappings-parser:0.3.0+build.17")!!)

    modImplementation(include("teamreborn:energy:${property("teamreborn_energy_version")}")!!)

    // Use Kilt's fork of Sinytra Connector's fork of ForgeAutoRenamingTool
    implementation(include("xyz.bluspring:AutoRenamingTool:${property("forgerenamer_version")}")!!)

    fun modOptional(dependencyNotation: String, shouldRunInRuntime: Boolean, configuration: Action<ExternalModuleDependency>) {
        if (shouldRunInRuntime) {
            modImplementation(dependencyNotation, configuration)
        } else {
            modCompileOnly(dependencyNotation, configuration)
        }
    }

    val runSodium = true

    // Runtime mods for testing
    modRuntimeOnly ("com.terraformersmc:modmenu:7.1.0") {
        exclude("net.fabricmc", "fabric-loader")
    }
    modRuntimeOnly ("maven.modrinth:ferrite-core:6.0.1-fabric") {
        exclude("net.fabricmc", "fabric-loader")
    }
    modOptional ("maven.modrinth:sodium:mc1.20.1-0.5.11", runSodium) {
        exclude("net.fabricmc", "fabric-loader")
    }
    modRuntimeOnly ("maven.modrinth:lithium:mc1.20.1-0.11.2") {
        exclude("net.fabricmc", "fabric-loader")
    }
    modOptional ("maven.modrinth:indium:1.0.34+mc1.20.1", runSodium) {
        exclude("net.fabricmc", "fabric-loader")
    }

    implementation(include("commons-codec:commons-codec:1.15")!!)
}

configurations.all {
    exclude("cpw.mods", "modlauncher")
}

interface ProviderFactoryHolder {
    @get:Inject
    val provider: ProviderFactory
}

tasks {
    val projectVersion = project.version

    processResources {
        val provider = objects.newInstance<ProviderFactoryHolder>().provider
        val propertiesMap = mutableMapOf(
            "version" to projectVersion,
            "loader_version" to provider.gradleProperty("loader_version"),
            "fabric_version" to provider.gradleProperty("fabric_version"),
            "minecraft_version" to provider.gradleProperty("minecraft_version"),
            "fabric_kotlin_version" to provider.gradleProperty("fabric_kotlin_version"),
            "fabric_asm_version" to provider.gradleProperty("fabric_asm_version"),
            "forge_config_version" to provider.gradleProperty("forgeconfigapiport_version"),
            "architectury_version" to provider.gradleProperty("architectury_version"),
        )
        propertiesMap.forEach { (key, value) -> inputs.property(key, value) }

        filteringCharset = "UTF-8"

        filesMatching("fabric.mod.json") {
            expand(propertiesMap)
        }

        // Rename Forge's mods.toml, so launchers like Prism don't end up detecting it over Kilt.
        filesMatching("META-INF/mods.toml") {
            this.name = "forge.mods.toml"
        }
    }
}

fun getVersionMetadata(): String {
    val grgit = Grgit.open(mutableMapOf<String, Any?>(
        "dir" to File("$projectDir")
    ))
    val commitHash =
        System.getenv("GITHUB_SHA") ?: grgit.head().abbreviatedId

    return "+build.${commitHash.subSequence(0, 6)}${if (System.getenv("GITHUB_RUN_NUMBER") == null) "-local" else ""}"
}