plugins {
	id("com.android.library")
	id("org.jetbrains.kotlin.android")
}

android {
	namespace = "ir.mservices.myketiap"
	compileSdk = 36
	buildToolsVersion = "36.0.0"

	sourceSets {
		getByName("main") {
			java.srcDirs("src/main/kotlin")
		}
	}

	defaultConfig {
		minSdk = 21
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
	implementation("ir.myket:billing:2.2")
	implementation("org.jetbrains.kotlin:kotlin-stdlib:2.2.20")
	implementation("com.google.code.gson:gson:2.10.1")
}
