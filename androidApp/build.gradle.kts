plugins {
    id("com.android.application")
    kotlin("android")
}

android {
    compileSdkVersion((properties["android.compileSdk"] as String).toInt())

    defaultConfig {
        minSdkVersion((properties["android.minSdk"] as String).toInt())
        targetSdkVersion((properties["android.targetSdk"] as String).toInt())
        buildToolsVersion = properties["android.buildToolsVersion"] as String

        applicationId = "com.github.jetbrains.rssreader.androidApp"
        versionCode = 1
        versionName = "1.0"
    }

    buildTypes {
        getByName("release") {
            isMinifyEnabled = false
        }
    }
}

dependencies {
    implementation(project(":shared"))
    implementation("com.google.android.material:material:1.2.1")
    implementation("androidx.appcompat:appcompat:1.2.0")
    implementation("androidx.constraintlayout:constraintlayout:2.0.1")
    //Coroutines
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:${properties["version.kotlinx.coroutines"]}")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-android:${properties["version.kotlinx.coroutines"]}")
}