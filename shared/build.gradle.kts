plugins {
    alias(libs.plugins.kotlinMultiplatform)
    alias(libs.plugins.androidKotlinMultiplatformLibrary)
    alias(libs.plugins.composeMultiplatform)
    alias(libs.plugins.composeCompiler)
}

kotlin {
    androidLibrary {
        namespace = "org.shad.adman.jaw.generation.shared"
        compileSdk = libs.versions.android.compileSdk.get().toInt()
        minSdk = libs.versions.android.minSdk.get().toInt()
        androidResources.enable = true
    }


    listOf(
        iosArm64(),
        iosSimulatorArm64()
    ).forEach { iosTarget ->
        iosTarget.binaries.framework {
            baseName = "shared"
            isStatic = true
        }
    }
    sourceSets {
        androidMain.dependencies {
            implementation(libs.accompanist.permissions)
            implementation(libs.ui.tooling)
        }
        commonMain.dependencies {
            api(libs.foundation)
            api(libs.components.resources)
            api(libs.ui.tooling.preview)
            api(libs.runtime)
            api(libs.material3)
            api(libs.ui)

            api(libs.koin.core)
            api(libs.koin.compose)
            api(libs.koin.viewmodel)

            api(project(":core"))
        }
    }

}

compose.resources {
    publicResClass = true
    packageOfResClass = "shared.resources"
    generateResClass = auto
}
