package com.kashif.cameraK.builder

import camera.builder.CameraControllerBuilder

/**
 * Creates an iOS-specific [CameraControllerBuilder].
 *
 * @return An instance of [CameraControllerBuilder].
 */
fun createIOSCameraControllerBuilder(): CameraControllerBuilder = IOSCameraControllerBuilder()
