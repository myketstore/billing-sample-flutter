package android

plugins {
	id("com.android.library")
	id("org.jetbrains.kotlin.android")
}

group = "ir.mservices.myketiap"
version = "1.0-SNAPSHOT"

android {
	namespace = "ir.mservices.myketiap"
	compileSdk = 34

	sourceSets {
		getByName("main") {
			java.srcDirs("src/main/kotlin")
		}
	}

	defaultConfig {
		minSdk = 16
		consumerProguardFiles("proguard-base.txt")
	}

	lint {
		disable.add("InvalidPackage")
	}

	kotlin {
		jvmToolchain(21)
	}
}

dependencies {
	implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk7:2.2.20")
	implementation("com.google.code.gson:gson:2.10.1")
	implementation("ir.myket:billing:2.2")
}
