package xyz.bluspring.kilt.gradle

plugins {
    `maven-publish`
    signing
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
