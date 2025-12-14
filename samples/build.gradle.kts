plugins {
	alias(libs.plugins.android.library)
	alias(libs.plugins.kotlin.android)
	alias(libs.plugins.kotlin.kapt)
}

android {
	namespace = "io.github.vafeen.samples"
	compileSdk {
		version = release(36)
	}

	defaultConfig {
		minSdk = 24

		testInstrumentationRunner = "android.support.test.runner.AndroidJUnitRunner"
		consumerProguardFiles("consumer-rules.pro")
	}

	buildTypes {
		release {
			isMinifyEnabled = false
			proguardFiles(
				getDefaultProguardFile("proguard-android-optimize.txt"),
				"proguard-rules.pro"
			)
		}
	}
	compileOptions {
		sourceCompatibility = JavaVersion.VERSION_11
		targetCompatibility = JavaVersion.VERSION_11
	}
	kotlinOptions {
		jvmTarget = "11"
	}
}

dependencies {
	implementation(libs.appcompat.v7)
	implementation(libs.androidx.core.ktx)
	testImplementation(libs.junit)
	androidTestImplementation(libs.runner)
	androidTestImplementation(libs.espresso.core)
	// dagger2
	implementation("com.google.dagger:dagger:2.57.2")
	kapt("com.google.dagger:dagger-compiler:2.57.2")
}