package xyz.bluspring.kilt.gradle.util

import org.gradle.api.artifacts.dsl.RepositoryHandler
import org.gradle.api.artifacts.repositories.MavenArtifactRepository
import org.gradle.kotlin.dsl.maven

fun MavenArtifactRepository.includeGroup(group: String) {
    content {
        includeGroup(group)
    }
}

fun RepositoryHandler.maven(name: String, url: String) =
    maven(url) {
        this.name = name
    }

fun RepositoryHandler.maven(name: String, url: String, configure: MavenArtifactRepository.() -> Unit) =
    maven(url) {
        this.name = name
        configure()
    }
