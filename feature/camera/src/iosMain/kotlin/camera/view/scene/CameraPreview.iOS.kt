package camera.view.scene


import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.viewinterop.UIKitViewController
import camera.view.builder.CameraControllerBuilder
import camera.view.controller.CameraController
import camera.view.builder.createIOSCameraControllerBuilder
import platform.Foundation.NSNotificationCenter
import platform.UIKit.UIDeviceOrientationDidChangeNotification

/**
 * iOS-specific implementation of [CameraPreview].
 *
 * @param modifier Modifier to be applied to the camera preview.
 * @param cameraConfiguration Lambda to configure the [CameraControllerBuilder].
 * @param onCameraControllerReady Callback invoked with the initialized [camera.view.controller.CameraController].
 */
@Composable
actual fun ExpectCameraPreview(
    modifier: Modifier,
    cameraConfiguration: CameraControllerBuilder.() -> Unit,
    onCameraControllerReady: (CameraController) -> Unit
) {

    val cameraController = remember {
        createIOSCameraControllerBuilder()
            .apply(cameraConfiguration)
            .build()
    }

    LaunchedEffect(cameraController) {
        onCameraControllerReady(cameraController)
    }

    DisposableEffect(Unit) {
        val notificationCenter = NSNotificationCenter.defaultCenter
        val observer = notificationCenter.addObserverForName(
            UIDeviceOrientationDidChangeNotification,
            null,
            null
        ) { _ ->
            cameraController.getCameraPreviewLayer()?.connection?.videoOrientation =
                cameraController.currentVideoOrientation()
        }

        onDispose {
            notificationCenter.removeObserver(observer)
        }
    }

    UIKitViewController(
        factory = { cameraController },
        modifier = modifier,
    )
}
