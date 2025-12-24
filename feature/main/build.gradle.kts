import org.jetbrains.kotlin.gradle.ExperimentalKotlinGradlePluginApi
import org.jetbrains.kotlin.gradle.dsl.JvmTarget

plugins {
    alias(libs.plugins.kotlinMultiplatform)
    alias(libs.plugins.androidKotlinMultiplatformLibrary)
    alias(libs.plugins.composeMultiplatform)
    alias(libs.plugins.composeCompiler)
}

kotlin {
    androidLibrary {
        namespace = "org.shad.adman.jaw.generation.main"
        compileSdk = libs.versions.android.compileSdk.get().toInt()
        minSdk = libs.versions.android.minSdk.get().toInt()
        androidResources.enable = true
    }

    listOf(
        iosArm64(),
        iosSimulatorArm64()
    ).forEach { iosTarget ->
        iosTarget.binaries.framework {
            baseName = "main"
            isStatic = true
        }
    }
    sourceSets {
        androidMain.dependencies {
            implementation("org.jetbrains.compose.ui:ui-tooling-preview:1.10.0-rc02")
            implementation("org.jetbrains.compose.ui:ui-tooling:1.10.0-rc02")
        }

        commonMain.dependencies {
            implementation("org.jetbrains.compose.ui:ui-tooling-preview:1.10.0-rc02")
            implementation("org.jetbrains.compose.runtime:runtime:1.10.0-beta01")
            implementation("org.jetbrains.compose.foundation:foundation:1.10.0-beta01")
            implementation("org.jetbrains.compose.material3:material3:1.9.0")
            implementation("org.jetbrains.compose.ui:ui:1.10.0-beta01")
            implementation("org.jetbrains.compose.components:components-resources:1.10.0-beta01")
            implementation(project(":shared"))
        }
    }
}