pluginManagement {
	plugins {
		id("com.android.library") version "8.12.3"
		id("org.jetbrains.kotlin.android") version "2.2.20"
	}
	repositories {
		mavenLocal()
		maven { url = uri("https://maven.myket.ir") }
		google()
		mavenCentral()
		gradlePluginPortal()
	}
}

dependencyResolutionManagement {
	repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
	repositories {
		mavenLocal()
		maven { url = uri("https://maven.myket.ir") }
		google()
		mavenCentral()
		maven { url = uri("https://jitpack.io") }
	}
}

rootProject.name = "myketiap"
