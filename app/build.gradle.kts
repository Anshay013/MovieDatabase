plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.compose)
    alias(libs.plugins.kotlin.serialization)
    id("com.google.devtools.ksp")
    alias(libs.plugins.hilt)
}

android {
    namespace = "com.example.moviedatabase"

    compileSdk = 36

    defaultConfig {
        applicationId = "com.example.moviedatabase"
        minSdk = 26
        targetSdk = 36
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"

        // Load API key from gradle.properties and ensure it's properly quoted for Java
        val apiKey = project.findProperty("MOVIE_API_KEY")?.toString()?.replace("\"", "") ?: ""
        buildConfigField("String", "MOVIE_API_KEY", "\"$apiKey\"")
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

    buildFeatures {
        viewBinding = true
        buildConfig = true
    }
}

kotlin {
    jvmToolchain(11)
}

dependencies {

    // Core Android
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.appcompat)
    implementation(libs.material)
    implementation(libs.constraintlayout)
    implementation(libs.swiperefreshlayout)

    // Lifecycle & ViewModel
    implementation(libs.lifecycle.viewmodel)

    // Coroutines
    implementation(libs.coroutines.android)

    // UI
    implementation(libs.coil)

    // =========================
    // Networking
    // =========================
    implementation(libs.retrofit.v2110)
    implementation(libs.okhttp.v4120)

    // Kotlin Serialization
    implementation(libs.serialization.json)
    implementation(libs.retrofit.serialization)

    // =========================
    // Room Database (KSP)
    // =========================
    implementation(libs.room.runtime)
    implementation(libs.room.ktx)
    implementation(libs.recyclerview)
    ksp(libs.room.compiler)

    // =========================
    // Hilt Dependency Injection
    // =========================
    implementation(libs.hilt.android)
    ksp(libs.hilt.compiler)

    // Hilt + Compose ViewModel
    implementation("androidx.hilt:hilt-navigation-compose:1.2.0")

    // =========================
    // Testing
    // =========================
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
}