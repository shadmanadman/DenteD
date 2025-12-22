package postprocessing

import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.graphics.Canvas
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.Paint
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.IntSize
import model.ToothBox

// Create a cropped bitmap of the tooth using the specified rectangle
fun ImageBitmap.cropInRect(rect: Rect): ImageBitmap {
    val left = rect.left.coerceIn(0f, this.width.toFloat())
    val top = rect.top.coerceIn(0f, this.height.toFloat())
    val right = rect.right.coerceIn(0f, this.width.toFloat())
    val bottom = rect.bottom.coerceIn(0f, this.height.toFloat())

    val cropX = left.toInt()
    val cropY = top.toInt()
    val cropWidth = (right - left).toInt()
    val cropHeight = (bottom - top).toInt()

        Canvas(this).drawImageRect(
            image = this,
            srcOffset = IntOffset(cropX, cropY),
            srcSize = IntSize(cropWidth, cropHeight),
            dstSize = IntSize(cropWidth, cropHeight),
            paint = Paint()
        )

    return this
}

/**
 * Visible boxes that can been seen in the screen with a small padding. the hole box should
 * be visible to count as visible.
 */
fun List<ToothBox>.filterVisibleBoxes(normalizedPadding: Float): List<ToothBox> {
    return this.filter { box ->
        println("box: $box")

        val originX = box.x - (box.width / 2)
        println("originX: $originX")

        val originY = box.y - (box.height / 2)
        println("originY: $originY")

        val endX = originX + box.width
        println("endX: $endX")

        val endY = originY + box.height
        println("endY: $endY")

        val padding = 0.005f

        originX - padding >= normalizedPadding && endX + padding <= 1 - normalizedPadding &&
                originY - padding >= 0 && endY + padding <= 1
    }
}

fun MutableList<ToothBox>.exportTeethCoordinate(): Array<DoubleArray> {
    val teethCoordinate = Array(this.size) { DoubleArray(5) }

    for ((index, box) in this.withIndex()) {
        teethCoordinate[index][0] =
            box.category.toDouble() // Assuming number represents the category ID
        teethCoordinate[index][1] = box.x.toDouble()
        teethCoordinate[index][2] = box.y.toDouble()
        teethCoordinate[index][3] = box.width.toDouble()
        teethCoordinate[index][4] = box.height.toDouble()
    }

    return teethCoordinate
}



// This will visualize a box for each detected row in a given bitmap. usually for testing purpose
//@Composable
//fun InsertDetectedToothBox(
//    detectionRow: List<ToothBox>,
//    modelInputSize: Int = 640
//): ImageBitmap {
//
//    val canvas = Canvas(this)
//
//    val boxPaint = Paint().apply {
//        color = Color.BLUE
//        style = Paint.Style.STROKE
//        strokeWidth = 1f
//    }
//
//    val textPaint = Paint().apply {
//        color = Color.RED
//        textSize = 25f
//        textAlign = Paint.Align.CENTER
//    }
//
//    detectionRow.forEach { item ->
//        // Convert relative center coords → top-left absolute coords
//        val x = (item.x - item.width / 2f) * modelInputSize
//        val y = (item.y - item.height / 2f) * modelInputSize
//        val w = item.width * modelInputSize
//        val h = item.height * modelInputSize
//
//        // Build rect
//        val rect = Rect(
//            left = x,
//            top = y,
//            right = x + w,
//            bottom = y + h
//        )
//
//        // Draw tooth number (centered horizontally)
//        val numX = x + w / 2f
//        val numY = y - 5f       // a bit above the box
//        canvas.drawText(item.alphabeticNumber, numX, numY, textPaint)
//
//        // Draw bounding box
//        canvas.drawRect(rect, boxPaint)
//    }
//}