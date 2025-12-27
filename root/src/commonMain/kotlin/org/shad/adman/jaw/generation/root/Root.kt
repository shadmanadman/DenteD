package org.shad.adman.jaw.generation.root

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.BlurredEdgeTreatment
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.blur
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import camera.scene.CameraPreview
import moe.tlaster.precompose.PreComposeApp
import moe.tlaster.precompose.navigation.NavHost
import moe.tlaster.precompose.navigation.Navigator
import moe.tlaster.precompose.navigation.rememberNavigator
import moe.tlaster.precompose.navigation.transition.NavTransition
import shared.navigation.MainNav
import shared.platform.PermissionCallback
import shared.platform.PermissionStatus
import shared.platform.PermissionType
import shared.platform.createPermissionsManager
import shared.theme.Black
import view.scene.MainScene

@Composable
fun Root() {
    PreComposeApp {
        val navigator = rememberNavigator()
        Box(modifier = Modifier.fillMaxSize()) {
            RootCameraPreview(cameraPreviewMode = defineCameraPreviewMode(navigator))

            NavHost(
                navigator = navigator,
                navTransition = NavTransition(),
                initialRoute = MainNav.main
            ) {
                scene(route = MainNav.main) {
                    MainScene(onNavigate = {
                        navigator.navigate(it)
                    })
                }
            }

        }
    }
}

private enum class CameraPreviewMode{PreviewBlurred,Preview,NoPreview}
@Composable
private fun defineCameraPreviewMode(navigator: Navigator): CameraPreviewMode {
    val currentEntry by navigator.currentEntry.collectAsState(null)
    val currentRoute = currentEntry?.route?.route
    return when (currentRoute) {
        MainNav.main -> CameraPreviewMode.PreviewBlurred
        else -> CameraPreviewMode.NoPreview
    }
}

@Composable
private fun RootCameraPreview(cameraPreviewMode: CameraPreviewMode) {
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
                cameraConfiguration = {},
                onCameraControllerReady = {})
        }
}