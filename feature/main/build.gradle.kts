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
            implementation(libs.ui.tooling)
        }

        commonMain.dependencies {
            implementation(libs.kotlin.coroutines)
            implementation(libs.liquid)

            api(project(":shared"))
        }
    }
}

dependencies {
    "androidRuntimeClasspath"(libs.androidx.compose.ui.tooling)
}