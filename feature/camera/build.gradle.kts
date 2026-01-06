plugins {
    alias(libs.plugins.kotlinMultiplatform)
    alias(libs.plugins.androidKotlinMultiplatformLibrary)
    alias(libs.plugins.composeMultiplatform)
    alias(libs.plugins.composeCompiler)
}

kotlin {

    androidLibrary {
        namespace = "org.shad.adman.jaw.generation.camera"
        compileSdk = libs.versions.android.compileSdk.get().toInt()
        minSdk = libs.versions.android.minSdk.get().toInt()
    }

    listOf(
            iosArm64(),
            iosSimulatorArm64()
    ).forEach { iosTarget ->
        iosTarget.binaries.framework {
            baseName = "camera"
            isStatic = true
        }
    }

    sourceSets {
        androidMain.dependencies {
            implementation(libs.androidx.camera)
            implementation(libs.androidx.camera2)
            implementation(libs.androidx.camera.core)
            implementation(libs.androidx.camera.lifecycle)
            implementation(libs.androidx.concurrent.futures.ktx)
            implementation(libs.guava.android)
        }
        commonMain.dependencies {
            implementation(libs.precompose.core)
            implementation(libs.ui.tooling.preview)
            implementation(libs.material3)

            implementation(libs.atomicfu)

            implementation(libs.kotlin.coroutines)

            implementation(project(":shared"))
            implementation(project(":feature:detection"))
        }
        commonTest.dependencies {
        }
    }
}

