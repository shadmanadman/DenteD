package camera.controller

import kotlinx.atomicfu.atomic
import kotlinx.cinterop.CValue
import kotlinx.cinterop.ExperimentalForeignApi
import model.FocusPoints
import platform.AVFoundation.AVCaptureMetadataOutput
import platform.AVFoundation.AVCaptureMetadataOutputObjectsDelegateProtocol
import platform.AVFoundation.AVCaptureTorchMode
import platform.AVFoundation.AVCaptureTorchModeAuto
import platform.AVFoundation.AVCaptureTorchModeOff
import platform.AVFoundation.AVCaptureTorchModeOn
import platform.AVFoundation.AVCaptureVideoOrientation
import platform.AVFoundation.AVCaptureVideoOrientationLandscapeLeft
import platform.AVFoundation.AVCaptureVideoOrientationLandscapeRight
import platform.AVFoundation.AVCaptureVideoOrientationPortrait
import platform.AVFoundation.AVCaptureVideoOrientationPortraitUpsideDown
import platform.CoreGraphics.CGPoint
import platform.CoreGraphics.CGFloat
import platform.CoreGraphics.CGPointMake
import platform.SharedImage
import platform.UIKit.UIDevice
import platform.UIKit.UIDeviceOrientation
import platform.UIKit.UIViewController

actual class CameraController(
    internal var torchMode: TorchMode,
    internal var cameraDeviceType: String
) : UIViewController(null, null) {
    private var isCapturing = atomic(false)
    private var metadataOutput = AVCaptureMetadataOutput()
    private var metadataObjectsDelegate: AVCaptureMetadataOutputObjectsDelegateProtocol? = null
    private val memoryManager = MemoryManager
    private val customCameraController = CustomCameraController()
    actual var onImageAvailable: ((SharedImage?) -> Unit)? = null


    override fun viewDidLoad() {
        super.viewDidLoad()

        memoryManager.initialize()
        setupCamera()
    }


    override fun viewWillAppear(animated: Boolean) {
        super.viewWillAppear(animated)
        memoryManager.updateMemoryStatus()
    }

    override fun viewDidDisappear(animated: Boolean) {
        super.viewDidDisappear(animated)

        memoryManager.clearBufferPools()
    }

    fun getCameraPreviewLayer() = customCameraController.cameraPreviewLayer

    internal fun currentVideoOrientation(): AVCaptureVideoOrientation {
        val orientation = UIDevice.currentDevice.orientation
        return when (orientation) {
            UIDeviceOrientation.UIDeviceOrientationPortrait -> AVCaptureVideoOrientationPortrait
            UIDeviceOrientation.UIDeviceOrientationPortraitUpsideDown -> AVCaptureVideoOrientationPortraitUpsideDown
            UIDeviceOrientation.UIDeviceOrientationLandscapeLeft -> AVCaptureVideoOrientationLandscapeRight
            UIDeviceOrientation.UIDeviceOrientationLandscapeRight -> AVCaptureVideoOrientationLandscapeLeft
            else -> AVCaptureVideoOrientationPortrait
        }
    }

    private fun setupCamera() {
        customCameraController.setupSession(cameraDeviceType)
        customCameraController.setupPreviewLayer(view)

        if (customCameraController.captureSession?.canAddOutput(metadataOutput) == true) {
            customCameraController.captureSession?.addOutput(metadataOutput)
        }

        startSession()

        customCameraController.onPhotoCapture = { image ->
            image?.let {
                //processImageCapture(it)
            }
        }

        customCameraController.onError = { error ->
            println("Camera Error: $error")
        }
    }

    @OptIn(ExperimentalForeignApi::class)
    override fun viewDidLayoutSubviews() {
        super.viewDidLayoutSubviews()
        customCameraController.cameraPreviewLayer?.setFrame(view.bounds)
    }

    actual fun setTorchMode(mode: TorchMode) {
        torchMode = mode
        customCameraController.setTorchMode(mode.toAVCaptureTorchMode())
    }


    actual fun startSession() {
        memoryManager.clearBufferPools()
        customCameraController.startSession()
    }

    actual fun stopSession() {
        customCameraController.stopSession()
    }

    @OptIn(ExperimentalForeignApi::class)
    actual suspend fun setFocus(focusPoints: FocusPoints): Boolean {
        customCameraController.setFocus(focusPoints.toCGPoints())
        return true
    }

    actual fun clearFocus() {
        customCameraController.clearFocus()
    }

    actual fun setZoom(zoomRatio: Float) {
        customCameraController.setZoom(zoomRatio.toDouble())
    }

    actual fun clearZoom() {
        customCameraController.clearFocus()
    }

    actual fun getMinFocusDistance(): Float {
        return customCameraController.getMinFocusDistance()?.toFloat() ?: 0f
    }

    actual fun getFOV(): Double {
        return customCameraController.getFOV()
    }


    private fun TorchMode.toAVCaptureTorchMode(): AVCaptureTorchMode = when (this) {
        TorchMode.ON -> AVCaptureTorchModeOn
        TorchMode.OFF -> AVCaptureTorchModeOff
        TorchMode.AUTO -> AVCaptureTorchModeAuto
    }

    @OptIn(ExperimentalForeignApi::class)
    private fun FocusPoints.toCGPoints(): CValue<CGPoint> {
        return CGPointMake(this.meteringPoint.x.toDouble(), this.meteringPoint.y.toDouble())
    }
}