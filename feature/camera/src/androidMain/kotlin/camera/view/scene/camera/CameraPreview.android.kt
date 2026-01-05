package camera.view.scene.camera

import androidx.camera.view.PreviewView
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.viewinterop.AndroidView
import androidx.lifecycle.compose.LocalLifecycleOwner
import camera.view.builder.CameraControllerBuilder
import camera.view.controller.CameraController
import camera.view.builder.createAndroidCameraControllerBuilder

/**
 * Android-specific implementation of [CameraPreview].
 *
 * @param modifier Modifier to be applied to the camera preview.
 * @param cameraConfiguration Lambda to configure the [CameraControllerBuilder].
 * @param onCameraControllerReady Callback invoked with the initialized [controller.CameraController].
 */
@Composable
actual fun ExpectCameraPreview(
    modifier: Modifier,
    cameraConfiguration: CameraControllerBuilder.() -> Unit,
    onCameraControllerReady: (CameraController) -> Unit
) {
    val context = LocalContext.current
    val lifecycleOwner = LocalLifecycleOwner.current

    val isCameraReady = remember { mutableStateOf(false) }
    val cameraController = remember {
        createAndroidCameraControllerBuilder(context, lifecycleOwner)
            .apply(cameraConfiguration)
            .build()
    }


    val previewView = remember { PreviewView(context) }

    DisposableEffect(previewView) {
        cameraController.bindCamera(previewView) {
            onCameraControllerReady(cameraController)
        }
        onDispose {
            cameraController.stopSession()
        }
    }

    AndroidView(
        factory = { previewView },
        modifier = modifier)
}