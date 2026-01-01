package camera.view.scene

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import camera.view.builder.CameraControllerBuilder
import camera.view.controller.CameraController


/**
 * Cross-platform composable function to display the camera preview.
 *
 * @param modifier Modifier to be applied to the camera preview.
 * @param cameraConfiguration Lambda to configure the [CameraControllerBuilder].
 * @param onCameraControllerReady Callback invoked with the initialized [controller.CameraController].
 */
@Composable
fun CameraPreview(
    modifier: Modifier = Modifier,
    cameraConfiguration: CameraControllerBuilder.() -> Unit,
    onCameraControllerReady: (CameraController) -> Unit,
) {
    ExpectCameraPreview(modifier, cameraConfiguration, onCameraControllerReady)
}

/**
 * Expects platform-specific implementation of [CameraPreview].
 */
@Composable
expect fun ExpectCameraPreview(
    modifier: Modifier,
    cameraConfiguration: CameraControllerBuilder.() -> Unit,
    onCameraControllerReady: (CameraController) -> Unit
)