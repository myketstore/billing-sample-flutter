package android

import org.gradle.api.initialization.resolve.RepositoriesMode
import org.gradle.kotlin.dsl.maven
import org.gradle.kotlin.dsl.version

pluginManagement {
	plugins {
		id("com.android.library") version "8.12.3"
		id("org.jetbrains.kotlin.android") version "2.2.20"
	}
	repositories {
		maven("https://maven.myket.ir")
		google()
		gradlePluginPortal()
		mavenCentral()
	}
}

dependencyResolutionManagement {
	repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
	repositories {
		maven("https://maven.myket.ir")
		google()
		mavenCentral()
	}
}

rootProject.name = "myketiap"
