package ext

import androidx.compose.ui.graphics.Canvas
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.Paint
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.IntSize

fun ImageBitmap.resize(newWidth: Int, newHeight: Int): ImageBitmap {
    val output = ImageBitmap(newWidth, newHeight)
    val canvas = Canvas(output)

    canvas.drawImageRect(
        image = this,
        srcOffset = IntOffset.Zero,
        srcSize = IntSize(width, height),
        dstOffset = IntOffset.Zero,
        dstSize = IntSize(newWidth, newHeight),
        paint = Paint()
    )

    return output
}


