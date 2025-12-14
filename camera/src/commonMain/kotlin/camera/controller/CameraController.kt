package camera.controller

import model.FocusPoints
import platform.SharedImage

expect class CameraController(){
    var onImageAvailable: ((SharedImage?) -> Unit)?
    fun setTorchMode(mode: TorchMode)
    suspend fun setFocus(focusPoints: FocusPoints): Boolean
    fun clearFocus()
    fun startSession()
    fun stopSession()
}

enum class ImageFormat(val extension: String, val mimeType: String) {
    JPEG("jpg", "image/jpeg"),
    PNG("png", "image/jpeg")
}

enum class CameraLens {
    FRONT,
    BACK
}

enum class TorchMode {
    ON,
    OFF,
    AUTO,
}