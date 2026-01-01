package camera.controller

import shared.model.FocusPoints
import shared.platform.SharedImage

expect class CameraController{
    var onImageAvailable: ((SharedImage?) -> Unit)?
    fun setTorchMode(mode: TorchMode)
    suspend fun setFocus(focusPoints: FocusPoints): Boolean
    fun clearFocus()
    fun setZoom(zoomRatio: Float)
    fun clearZoom()
    fun getMinFocusDistance(): Float
    fun getFOV(): Double
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