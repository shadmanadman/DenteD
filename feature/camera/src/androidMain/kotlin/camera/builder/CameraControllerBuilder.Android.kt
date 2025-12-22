package com.kashif.cameraK.builder

import android.content.Context
import androidx.lifecycle.LifecycleOwner
import camera.builder.CameraControllerBuilder
import camera.controller.CameraController
import camera.controller.TorchMode


/**
 * Android-specific implementation of [CameraControllerBuilder].
 *
 * @param context The Android [Context], typically an Activity or Application context.
 * @param lifecycleOwner The [LifecycleOwner], usually the hosting Activity or Fragment.
 */
class AndroidCameraControllerBuilder(
    private val context: Context,
    private val lifecycleOwner: LifecycleOwner
) : CameraControllerBuilder {

    private var torchMode: TorchMode = TorchMode.AUTO


    override fun setTorchMode(torchMode: TorchMode): CameraControllerBuilder {
        this.torchMode = torchMode
        return this
    }

    override fun build(): CameraController {
        val cameraController = CameraController(
            context = context,
            lifecycleOwner = lifecycleOwner,
            torchMode = torchMode
        )

        return cameraController
    }
}