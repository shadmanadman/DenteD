package shared.ext

import androidx.compose.ui.graphics.Canvas
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.ImageBitmapConfig
import androidx.compose.ui.graphics.Paint
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.IntRect
import androidx.compose.ui.unit.IntSize
import shared.platform.SharedImage

fun SharedImage.resize(newWidth: Int = 640, newHeight: Int = 640): SharedImage {
    val imageBitmap = this.toImageBitmap()
    val output = ImageBitmap(newWidth, newHeight)
    val canvas = Canvas(output)

    imageBitmap?.let {
        canvas.drawImageRect(
            image = it,
            srcOffset = IntOffset.Zero,
            srcSize = IntSize(it.width, it.height),
            dstOffset = IntOffset.Zero,
            dstSize = IntSize(newWidth, newHeight),
            paint = Paint()
        )
    }

    return SharedImage.fromImageBitmap(output)
}


fun SharedImage.cropWithIntRect(rect: IntRect): SharedImage {
    val croppedToothBitmap = ImageBitmap(rect.width, rect.height)
    val canvas = Canvas(croppedToothBitmap)

    canvas.drawImageRect(
        image = this.toImageBitmap()!!,
        srcOffset = rect.topLeft,
        srcSize = rect.size,
        dstOffset = IntOffset.Zero,
        dstSize = rect.size,
        paint = Paint()
    )

    return SharedImage.fromImageBitmap(croppedToothBitmap)
}

fun SharedImage.cropWithCoordination(
    x: Int,
    y: Int,
    width: Int,
    height: Int
): SharedImage {
    val newImage = ImageBitmap(width, height, ImageBitmapConfig.Argb8888)
    val canvas = Canvas(newImage)

    canvas.drawImageRect(
        image = this.toImageBitmap()!!,
        srcOffset = IntOffset(x, y),
        srcSize = IntSize(width, height),
        dstOffset = IntOffset.Zero,
        dstSize = IntSize(width, height),
        paint = Paint()
    )
    return SharedImage.fromImageBitmap(newImage)
}

