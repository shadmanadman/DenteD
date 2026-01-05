package org.shad.adman.jaw.generation.root

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import camera.view.controller.CameraController
import camera.view.controller.TorchMode
import camera.view.scene.camera.CameraPreview
import camera.view.scene.camera.CameraScene
import jaw.view.scene.SelectionScene
import moe.tlaster.precompose.PreComposeApp
import moe.tlaster.precompose.navigation.NavHost
import moe.tlaster.precompose.navigation.Navigator
import moe.tlaster.precompose.navigation.rememberNavigator
import moe.tlaster.precompose.navigation.transition.NavTransition
import org.koin.compose.KoinApplication
import org.shad.adman.jaw.generation.root.di.appModules
import shared.navigation.CameraNav
import shared.navigation.MainNav
import shared.platform.PermissionCallback
import shared.platform.PermissionStatus
import shared.platform.PermissionType
import shared.platform.createPermissionsManager
import view.scene.MainScene

@Composable
fun Root() {
    KoinApplication(application = { modules(modules = appModules()) }) {
        PreComposeApp {
            val navigator = rememberNavigator()
            var cameraController: CameraController? = null
            Box(modifier = Modifier.fillMaxSize()) {

                RootCameraPreview(
                    cameraPreviewMode = defineCameraPreviewMode(navigator),
                    onCameraControllerReady = {
                        cameraController = it
                    })

                NavHost(
                    modifier = Modifier.padding(top = 34.dp),
                    navigator = navigator,
                    navTransition = NavTransition(),
                    initialRoute = MainNav.main.path
                ) {
                    // Main
                    scene(route = MainNav.main.path) {
                        MainScene(onNavigate = {
                            navigator.navigate(it.path)
                        })
                    }
                    // Jaw selection
                    scene(route = MainNav.selection.path) {
                        SelectionScene(onNavigate = {
                            navigator.navigate(it.path)
                        })
                    }
                    // Camera
                    scene(route = CameraNav.detection.path) {
                        CameraScene(cameraController)
                    }

                }

            }
        }
    }
}

private enum class CameraPreviewMode { PreviewBlurred, Preview, NoPreview }

@Composable
private fun defineCameraPreviewMode(navigator: Navigator): CameraPreviewMode {
    val currentEntry by navigator.currentEntry.collectAsState(null)
    val currentRoute = currentEntry?.route?.route
    return when (currentRoute) {
        MainNav.main.path -> CameraPreviewMode.PreviewBlurred
        MainNav.selection.path -> CameraPreviewMode.PreviewBlurred
        CameraNav.detection.path -> CameraPreviewMode.Preview
        else -> CameraPreviewMode.NoPreview
    }
}

/**
 * We are using the camera preview across multiple screens, so it make sense to have it right at root.
 */
@Composable
private fun RootCameraPreview(
    cameraPreviewMode: CameraPreviewMode,
    onCameraControllerReady: (CameraController) -> Unit
) {
    var cameraPermissionState by remember { mutableStateOf(false) }

    val permissionsManager = createPermissionsManager(object : PermissionCallback {
        override fun onPermissionStatus(
            permissionType: PermissionType,
            status: PermissionStatus
        ) {
            when (status) {
                PermissionStatus.GRANTED -> {
                    when (permissionType) {
                        PermissionType.CAMERA -> {
                            cameraPermissionState = true
                        }
                    }
                }

                PermissionStatus.DENIED -> {}
                PermissionStatus.SHOW_RATIONAL -> {}
            }
        }
    })

    if (permissionsManager.isPermissionGranted(PermissionType.CAMERA).not())
        permissionsManager.AskPermission(PermissionType.CAMERA)
    else
        cameraPermissionState = true

    if (cameraPermissionState && (cameraPreviewMode == CameraPreviewMode.PreviewBlurred || cameraPreviewMode == CameraPreviewMode.Preview))
        Box(modifier = Modifier.fillMaxSize()) {
            CameraPreview(
                modifier = Modifier.fillMaxSize(),
                cameraConfiguration = {
                    if (cameraPreviewMode == CameraPreviewMode.Preview)
                        setTorchMode(TorchMode.ON)
                },
                onCameraControllerReady = onCameraControllerReady
            )
        }
}