package shared.platform

import androidx.compose.ui.graphics.ImageBitmap

expect class SharedImage {
    fun toByteArray(): ByteArray?
    fun toImageBitmap(): ImageBitmap?
}

expect fun ImageBitmap.toByteArray(): ByteArray