pluginManagement {
	val flutterSdkPath =
		run {
			val properties = java.util.Properties()
			file("local.properties").inputStream().use { properties.load(it) }
			val flutterSdkPath = properties.getProperty("flutter.sdk")
			require(flutterSdkPath != null) { "flutter.sdk not set in local.properties" }
			flutterSdkPath
		}

	includeBuild("$flutterSdkPath/packages/flutter_tools/gradle")

	repositories {
		maven("https://maven.myket.ir")
		google()
		gradlePluginPortal()
		mavenCentral()
	}
}

plugins {
	id("dev.flutter.flutter-plugin-loader") version "4.1.2"
	id("com.android.application") version "8.12.3" apply false
	id("org.jetbrains.kotlin.android") version "2.2.20" apply false
}

dependencyResolutionManagement {
	repositoriesMode.set(RepositoriesMode.PREFER_SETTINGS)
	repositories {
		maven("https://maven.myket.ir")
		google()
		mavenCentral()
	}
}

include(":app")
