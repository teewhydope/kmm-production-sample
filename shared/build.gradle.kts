import org.jetbrains.kotlin.gradle.plugin.mpp.KotlinNativeTarget

plugins {
    kotlin("multiplatform")
    id("com.android.library")
}


kotlin {
    android()
    ios {
        binaries {
            framework {
                baseName = "shared"
            }
        }
    }

    sourceSets {
        commonMain {
            dependencies {
                //Network
                implementation("io.ktor:ktor-client-core:${properties["version.ktor"]}")
                implementation("io.ktor:ktor-client-logging:${properties["version.ktor"]}")
                //Coroutines
                implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:${properties["version.kotlinx.coroutines"]}")
                //Logger
                implementation("com.github.aakira:napier:${properties["version.napier"]}")
            }
        }
        val androidMain by getting {
            dependencies {
                //Network
                implementation("io.ktor:ktor-client-okhttp:${properties["version.ktor"]}")
            }
        }
    }
}

android {
    compileSdkVersion((properties["android.compileSdk"] as String).toInt())
    defaultConfig {
        minSdkVersion((properties["android.minSdk"] as String).toInt())
        targetSdkVersion((properties["android.targetSdk"] as String).toInt())
    }
    sourceSets["main"].manifest.srcFile("src/androidMain/AndroidManifest.xml")
}

val packForXcode by tasks.creating(Sync::class) {
    group = "build"
    val mode = System.getenv("CONFIGURATION") ?: "DEBUG"
    val sdkName = System.getenv("SDK_NAME") ?: "iphonesimulator"
    val targetName = "ios" + if (sdkName.startsWith("iphoneos")) "Arm64" else "X64"
    val framework = kotlin.targets.getByName<KotlinNativeTarget>(targetName).binaries.getFramework(mode)
    inputs.property("mode", mode)
    dependsOn(framework.linkTask)
    val targetDir = File(buildDir, "xcode-frameworks")
    from({ framework.outputDirectory })
    into(targetDir)
}
tasks.getByName("build").dependsOn(packForXcode)