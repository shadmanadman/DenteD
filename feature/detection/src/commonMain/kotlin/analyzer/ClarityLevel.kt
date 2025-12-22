package analyzer

import androidx.compose.ui.graphics.Canvas
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.ColorMatrix
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.ImageBitmapConfig
import androidx.compose.ui.graphics.Paint
import androidx.compose.ui.graphics.toPixelMap

object ClarityLevel {

    fun convertToGrayscale(source: ImageBitmap): ImageBitmap {
        val width = source.width
        val height = source.height

        // Output image
        val output = ImageBitmap(width, height, ImageBitmapConfig.Argb8888)

        // Canvas for drawing onto output
        val canvas = Canvas(output)

        // Paint with grayscale color filter
        val paint = Paint().apply {
            colorFilter = ColorFilter.colorMatrix(
                ColorMatrix().apply { setToSaturation(0f) }
            )
        }

        // Draw full image using the grayscale paint
        canvas.drawImage(
            image = source,
            topLeftOffset = androidx.compose.ui.geometry.Offset.Zero,
            paint = paint
        )

        return output
    }


//    fun applySobelFilter(source: ImageBitmap): ImageBitmap {
//        val width = source.width
//        val height = source.height
//
//        // Read pixel buffer
//        val srcPixels = IntArray(width * height)
//        source.readPixels(srcPixels, startX = 0, startY = 0, width = width, height = height)
//
//        val outPixels = IntArray(width * height)
//
//        val sobelX = arrayOf(
//            intArrayOf(-1, 0, 1),
//            intArrayOf(-2, 0, 2),
//            intArrayOf(-1, 0, 1)
//        )
//        val sobelY = arrayOf(
//            intArrayOf(-1, -2, -1),
//            intArrayOf(0, 0, 0),
//            intArrayOf(1, 2, 1)
//        )
//
//        fun getGray(pixel: Int): Int {
//            val r = (pixel shr 16) and 0xFF
//            val g = (pixel shr 8) and 0xFF
//            val b = (pixel) and 0xFF
//            return (0.299f * r + 0.587f * g + 0.114f * b).toInt()
//        }
//
//        fun clamp(value: Int, minV: Int = 0, maxV: Int = 255): Int =
//            max(minV, min(maxV, value))
//
//        // Apply Sobel filter
//        for (y in 1 until height - 1) {
//            for (x in 1 until width - 1) {
//                var gx = 0
//                var gy = 0
//
//                for (i in 0..2) {
//                    for (j in 0..2) {
//                        val px = x + i - 1
//                        val py = y + j - 1
//                        val pixel = srcPixels[py * width + px]
//                        val gray = getGray(pixel)
//
//                        gx += gray * sobelX[i][j]
//                        gy += gray * sobelY[i][j]
//                    }
//                }
//
//                val magnitude = clamp(sqrt((gx * gx + gy * gy).toDouble()).toInt())
//                val grayColor = (0xFF shl 24) or (magnitude shl 16) or (magnitude shl 8) or magnitude
//
//                outPixels[y * width + x] = grayColor
//            }
//        }
//
//        // Create output ImageBitmap
//        return ImageBitmap(
//            width = width,
//            height = height,
//            config = ImageBitmapConfig.Argb8888
//        ).also { out ->
//            out.writePixels(outPixels, startX = 0, startY = 0, width = width, height = height)
//        }
//    }


//    fun applyThreshold(
//        source: ImageBitmap,
//        threshold: Int = 128
//    ): ImageBitmap {
//
//        val width = source.width
//        val height = source.height
//
//        // --- Read pixels from ImageBitmap ---
//        val srcPixels = IntArray(width * height)
//        source.readPixels(
//            buffer = srcPixels,
//            startX = 0,
//            startY = 0,
//            width = width,
//            height = height
//        )
//
//        // --- Transform ---
//        val outPixels = ByteArray(width * height)
//
//        for (i in srcPixels.indices) {
//            val pixel = srcPixels[i]
//
//            val r = (pixel shr 16) and 0xFF
//            val g = (pixel shr 8) and 0xFF
//            val b = pixel and 0xFF
//
//            // Standard grayscale intensity
//            val intensity = (0.299 * r + 0.587 * g + 0.114 * b).toInt()
//
//            val out = if (intensity > threshold)
//                0xFFFFFFFF.toFloat()  // white
//            else
//                0xFF000000.toFloat() // black
//
//            outPixels[i] = out
//        }
//
//        val skiaBitmap = Bitmap().apply {
//            allocPixels(
//                ImageInfo.makeS32(width, height, ColorAlphaType.PREMUL)
//            )
//            installPixels(outPixels)
//        }
//
//        return skiaBitmap.
//    }


    private fun calculateAverageGrayValue(bitmap: ImageBitmap): Double {
        val pixelMap = bitmap.toPixelMap()
        val width = pixelMap.width
        val height = pixelMap.height

        var blackPixelCount = 0
        val totalPixelCount = width * height

        for (y in 0 until height) {
            for (x in 0 until width) {
                val color = pixelMap[x, y]
                val intensity = (color.red * 255).toInt()

                // Treat pure black as black pixel
                if (intensity == 0) {
                    blackPixelCount++
                }
            }
        }

        return (blackPixelCount.toDouble() / totalPixelCount) * 100
    }

//    @OptIn(ExperimentalCoroutinesApi::class)
//    fun CoroutineScope.determineClarityLevel(bitmap: ImageBitmap) = produce {
//        val grayscaleBitmap = convertToGrayscale(bitmap)
//        val sobelBitmap = applySobelFilter(grayscaleBitmap)
//        val thresholdBitmap = applyThreshold(sobelBitmap)
//        send(calculateAverageGrayValue(thresholdBitmap))
//    }
}