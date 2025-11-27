package litert

import androidx.compose.ui.graphics.ImageBitmap

typealias TensorBuffer = Any
expect fun ImageBitmap.toScaledByteBuffer(
    inputWidth: Int,
    inputHeight: Int,
    inputAllocateSize: Int,
    normalize: Boolean = false
): TensorBuffer