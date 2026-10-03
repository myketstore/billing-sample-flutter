plugins {
	id("com.android.application")
	id("kotlin-android")
	id("dev.flutter.flutter-gradle-plugin")
}

android {
	namespace = "com.example.android.trivialdrivesample"
	compileSdk = 34

	sourceSets {
		getByName("main") {
			java.srcDirs("src/main/kotlin")
		}
	}

	defaultConfig {
		applicationId = "com.example.android.trivialdrivesample"
		minSdk = flutter.minSdkVersion
		targetSdk = flutter.targetSdkVersion
		versionCode = flutter.versionCode
		versionName = flutter.versionName

		val marketApplicationId = "ir.mservices.market"
		val marketBindAddress = "ir.mservices.market.InAppBillingService.BIND"
		manifestPlaceholders.putAll(
			mapOf(
				"marketApplicationId" to marketApplicationId,
				"marketBindAddress" to marketBindAddress,
				"marketPermission" to "$marketApplicationId.BILLING"
			)
		)
	}

	buildTypes {
		release {
			signingConfig = signingConfigs.getByName("debug")
		}
	}

	lint {
		disable.add("InvalidPackage")
		disable.add("ExpiredTargetSdkVersion")
	}

	kotlin {
		jvmToolchain(21)
	}
}

flutter {
	source = "../.."
}
