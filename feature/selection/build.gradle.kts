plugins {
    alias(libs.plugins.kotlinMultiplatform)
    alias(libs.plugins.androidKotlinMultiplatformLibrary)
    alias(libs.plugins.composeCompiler)
    alias(libs.plugins.composeMultiplatform)
}

kotlin {

    androidLibrary {
        namespace = "org.shad.adman.jaw.generation.selection"
        compileSdk = libs.versions.android.compileSdk.get().toInt()
        minSdk = libs.versions.android.minSdk.get().toInt()
    }

    listOf(
        iosArm64(),
        iosSimulatorArm64()
    ).forEach { iosTarget ->
        iosTarget.binaries.framework {
            baseName = "selection"
            isStatic = true
        }
    }
    sourceSets {
        androidMain.dependencies {
            implementation(libs.ui.tooling.preview)
            implementation(libs.ui.tooling)
        }
        commonMain.dependencies {
            implementation(libs.ui.tooling.preview)
            implementation(libs.runtime)
            implementation(libs.foundation)
            implementation(libs.material3)
            implementation(libs.ui)
            implementation(libs.components.resources)
            implementation(libs.androidx.constraintlayout.compose)
            implementation(project(":shared"))
        }
    }
}



