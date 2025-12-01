package litert

import androidx.compose.ui.graphics.ImageBitmap

typealias PlatformTensorBuffer = Any
expect fun ImageBitmap.toScaledByteBuffer(
    inputWidth: Float = 640f,
    inputHeight: Float = 640f,
    inputAllocateSize: Int,
    normalize: Boolean = false
): PlatformTensorBuffer