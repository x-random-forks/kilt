plugins {
    `kotlin-dsl`
}

repositories {
    mavenCentral()
    maven("https://maven.fabricmc.net")
}

fun gradlePlugin(id: String, version: String? = null): String =
    "$id:$id.gradle.plugin${version?.let { ":$it" }}"

dependencies {
    compileOnly(gradlePlugin("org.jetbrains.kotlin.jvm", "2.1.0"))
    implementation(gradlePlugin("fabric-loom", "1.9.2"))

    implementation("org.ow2.asm:asm-tree:9.7.1")
    implementation("net.fabricmc:mapping-io:0.5.1")
    implementation("net.fabricmc:tiny-mappings-parser:0.3.0+build.17")
    implementation("com.google.code.gson:gson:2.10.1")
}