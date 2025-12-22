package com.kashif.cameraK.builder


import camera.builder.CameraControllerBuilder
import camera.controller.CameraController
import camera.controller.TorchMode
import platform.AVFoundation.AVCaptureDeviceTypeBuiltInWideAngleCamera

/**
 * iOS-specific implementation of [CameraControllerBuilder].
 */
class IOSCameraControllerBuilder : CameraControllerBuilder {

    private var torchMode: TorchMode = TorchMode.OFF
    private var cameraDeviceType: String? = AVCaptureDeviceTypeBuiltInWideAngleCamera

    override fun setTorchMode(torchMode: TorchMode): CameraControllerBuilder {
        this.torchMode = torchMode
        return this
    }

    fun setCameraDeviceType(cameraDeviceType: String?): CameraControllerBuilder {
        this.cameraDeviceType = cameraDeviceType
        return this
    }


    override fun build(): CameraController {
        val cameraController =
            CameraController(torchMode = torchMode, cameraDeviceType = cameraDeviceType ?: "")
        return cameraController
    }
}
