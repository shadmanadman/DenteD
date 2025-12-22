plugins {
    alias(libs.plugins.kotlinMultiplatform)
    alias(libs.plugins.androidKotlinMultiplatformLibrary)
    alias(libs.plugins.composeMultiplatform)
    alias(libs.plugins.composeCompiler)
    alias(libs.plugins.kotlinCocoapods)
}

kotlin {
    androidLibrary {
        namespace = "org.shad.adman.jaw.generation.detection"
        compileSdk = libs.versions.android.compileSdk.get().toInt()
        minSdk = libs.versions.android.minSdk.get().toInt()
    }


    iosArm64()
    iosSimulatorArm64()
    cocoapods {
        summary = "Inject TensorFlow Lite into iOS"
        homepage = "detection"
        version = "1.2.0"
        ios.deploymentTarget = "16"
        podfile = project.file("../../iosApp/Podfile")

        pod("TensorFlowLiteObjC", moduleName = "TFLTensorFlowLite")
        pod("TensorFlowLiteObjC/Metal") {
            linkOnly = true
        }
        pod("TensorFlowLiteObjC/CoreML") {
            linkOnly = true
        }


        framework {
            baseName = "detection"
            isStatic = true
            linkerOpts(
                project.file("../../iosApp/Pods/TensorFlowLiteObjC/Frameworks").path.let { "-F$it" },
                "-framework", "TensorFlowLiteObjC"
            )
        }
    }


    sourceSets {
        androidMain.dependencies {
            implementation(compose.ui)
            implementation(libs.androidx.activity.compose)
            implementation(libs.androidx.startup)
            implementation(libs.litert)
        }
        commonMain.dependencies {
            //implementation(libs.skiko)
            api(compose.foundation)
            api(project(":shared"))
        }
        commonTest.dependencies {
            implementation(libs.kotlin.test)
        }
    }
}