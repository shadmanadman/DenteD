package postprocessing

import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.unit.IntRect
import shared.ext.cropWithCoordination
import shared.model.JawSide
import shared.model.JawType
import shared.model.ToothBox
import shared.platform.SharedImage
import kotlin.math.max

private const val TENSOR_IMAGE_SIZE = 640F
private const val IOU_THRESHOLD = 0.50F
private const val NUM_CHANNELS = 40
private const val NUM_ELEMENTS = 8400

object OutputProcessing {

    /**
     * Processes the raw detection results from the object detection model and returns a list of valid tooth detections.
     *
     * @param data The 3D array of floats representing the raw output of the detection model.
     * @param isFrontModel A boolean flag indicating whether the model is for frontal images or not.
     *
     * @return A list of DetectionRow objects representing detected teeth that meet the confidence threshold.
     */
    fun processResults(
        data: Array<Array<FloatArray>>,
        isFrontModel: Boolean = false
    ): List<ToothBox> {
        val validRows = mutableListOf<ToothBox>()

        for (i in 0 until NUM_ELEMENTS) {
            // Extracting class scores
            val classScores = if (isFrontModel) {
                listOf(
                    data[0][i][4],
                    data[0][i][5],
                    data[0][i][6],
                    data[0][i][7],
                    data[0][i][8],
                    data[0][i][9],
                    data[0][i][10],
                    data[0][i][11]
                )
            } else
                listOf(
                    data[0][i][4],
                    data[0][i][5],
                    data[0][i][6],
                    data[0][i][7]
                )

            // Finding the maximum class score and its corresponding index
            val maxScoreIndex = classScores.indexOf(classScores.maxOrNull())
            val maxScore = classScores[maxScoreIndex]

            // Check if the maximum score is above the threshold
            if (maxScore > IOU_THRESHOLD) {
                println(maxScore)

                val normalizeConstant: Float = TENSOR_IMAGE_SIZE

                val normalizedX = data[0][i][0] / normalizeConstant
                val normalizedY = data[0][i][1] / normalizeConstant
                val normalizedWidth = data[0][i][2] / normalizeConstant
                val normalizedHeight = data[0][i][3] / normalizeConstant

                val validBox = ToothBox(
                    category = maxScoreIndex,
                    x = normalizedX,
                    y = normalizedY,
                    width = normalizedWidth,
                    height = normalizedHeight,
                    maxConf = maxScore,
                    number = 0,
                    alphabeticNumber = "")

                validRows.add(
                    validBox
                )
            }
        }

        println("Finish process detecting result")
        return validRows
    }


    /**
     * Determine if the user should be moving the device closer or further away.
     */
   fun calculateDistanceBetweenDeviceAndTooth(
        visibleBoxesCount: Int,
        missedCount: Int,
        jawSide: JawSide,
        jawType: JawType,
        onMoveCloser: () -> Unit,
        onMoveMuchCloser: () -> Unit,
        onMoveAway: () -> Unit,
        onMoveMuchAway: () -> Unit
    ): Boolean {

        println("current tooth count ${visibleBoxesCount + missedCount}")

        val acceptableToothCount = visibleBoxesCount + missedCount

        if (jawType == JawType.FRONT || jawSide == JawSide.MIDDLE)
            return false

        if (jawSide == JawSide.LEFT || jawSide == JawSide.RIGHT) {
            if (acceptableToothCount < 5) {
                if (acceptableToothCount < 4) {
                    onMoveMuchAway()
                } else {
                    onMoveAway()
                }
                return true
            } else if (acceptableToothCount in 5..6) {
                return false
            } else {
                if (acceptableToothCount > 7) {
                    onMoveMuchCloser()
                } else {
                    onMoveCloser()
                }
                return true
            }
        }
        return false
    }

    fun calculateOverlap(box1: ToothBox, box2: ToothBox): Float {
        val x1 = box1.x
        val y1 = box1.y
        val w1 = box1.width
        val h1 = box1.height

        val x2 = box2.x
        val y2 = box2.y
        val w2 = box2.width
        val h2 = box2.height

        // Calculate the coordinates of the intersection rectangle
        val xIntersection =
            maxOf(0.0F, minOf(x1 + w1 / 2, x2 + w2 / 2) - maxOf(x1 - w1 / 2, x2 - w2 / 2))
        val yIntersection =
            maxOf(0.0F, minOf(y1 + h1 / 2, y2 + h2 / 2) - maxOf(y1 - h1 / 2, y2 - h2 / 2))

        // Calculate the area of the intersection rectangle
        val areaIntersection = xIntersection * yIntersection

        // Calculate the area of both boxes
        val areaBox1 = w1 * h1
        val areaBox2 = w2 * h2

        // Calculate the overlap ratio
        return areaIntersection / (areaBox1 + areaBox2 - areaIntersection)
    }

    /**
     * Applies Non-Maximum Suppression (NMS) to a list of detected tooth bounding boxes to eliminate redundant detections.
     *
     *This function takes a list of ToothCategoryBox objects, sorts them by confidence score, and iteratively selects boxes
     * with low overlap (below IOU_THRESHOLD) to produce a final list of non-overlapping detections.
     *
     * @param boxes The list of detected tooth bounding boxes.
     * @returnA list of non-overlapping ToothCategoryBox objects after applying NMS.
     */
    fun nonMaxSuppression(boxes: List<ToothBox>): List<ToothBox> {
        if (boxes.isEmpty()) {
            return emptyList()
        }

        val sortedBoxes = boxes.sortedByDescending { it.maxConf }

        // Initialize a list to store the selected boxes
        val selectedBoxes = mutableListOf<ToothBox>()

        for (i in sortedBoxes.indices) {
            val currentBox = sortedBoxes[i]

            // Add the first box to the selected boxes list
            if (selectedBoxes.isEmpty()) {
                selectedBoxes.add(currentBox)
                continue
            }

            // Calculate the overlap with the current selected boxes
            val overlaps = selectedBoxes.map { calculateOverlap(box1 = currentBox, box2 = it) }
            val maxOverlap = overlaps.maxOrNull() ?: 0.0F

            // If the overlap is less than a threshold, add the current box to the selected boxes list
            if (maxOverlap < IOU_THRESHOLD) {
                selectedBoxes.add(currentBox)
            }
        }

        return selectedBoxes
    }

    /**
     * Cropping from the first top tooth until the last one on each detected/finished side
     */
    fun sideCroppingProcess(
        originalImage: SharedImage,
        boxes: List<ToothBox>
    ): SharedImage? {

        val minXBox = boxes.minByOrNull { it.x } ?: return null
        val minYBox = boxes.minByOrNull { it.y } ?: return null
        val maxXBox = boxes.maxByOrNull { it.x } ?: return null
        val maxYBox = boxes.maxByOrNull { it.y } ?: return null

        // Assuming coordinates are normalized (0.0 to 1.0) and mapped to 640px
        val originX = (minXBox.x - (minXBox.width / 2f)) * 640
        val originY = (minYBox.y - (minYBox.height / 2f)) * 640
        val endX = (maxXBox.x + (maxXBox.width / 2f)) * 640
        val endY = (maxYBox.y + (maxYBox.height / 2f)) * 640

        val width = endX - originX
        val height = endY - originY

        val customPadding = 10f
        val size = max(width, height) + 2 * customPadding

        // Calculate Square Bounds
        var left: Float
        var top: Float

        if (width > height) {
            left = originX - customPadding
            val centerY = originY + (height / 2f)
            top = centerY - (size / 2f)
        } else {
            top = originY - customPadding
            val centerX = originX + (width / 2f)
            left = centerX - (size / 2f)
        }

        val originalImageAsImageBitmap = originalImage.toImageBitmap()
        val originalImageWidth = originalImageAsImageBitmap?.width ?: 0
        val originalImageHeight = originalImageAsImageBitmap?.height ?: 0

        // Constraints & Final Dimensions
        val finalLeft = left.coerceIn(0f, originalImageWidth.toFloat()).toInt()
        val finalTop = top.coerceIn(0f, originalImageHeight.toFloat()).toInt()
        val finalWidth = (size).toInt().coerceAtMost(originalImageWidth - finalLeft)
        val finalHeight = (size).toInt().coerceAtMost(originalImageHeight - finalTop)

        if (finalWidth <= 0 || finalHeight <= 0) return null

        return originalImage.cropWithCoordination(finalLeft, finalTop, finalWidth, finalHeight)
    }



    /**
     * Post-processing of the detection results, Reshape the output buffer to [1 8400 40].
     * the current output buffer is [1 40 8400]. we need 8400*40=336000.
     */
//    fun reshapeByteBuffer(
//        tensorBuffer: ByteBuffer, isFrontModel: Boolean = false
//    ): ByteBuffer {
//        val batchSize = 1
//        val numChannels = if (isFrontModel) 12 else 8
//        val numElements = 8400
//        val dataType = DataType.FLOAT32
//
//        println("NUM CHANNELS:$numChannels")
//        // Create a new ByteBuffer with the desired shape
//        val reshapedByteBuffer =
//            ByteBuffer.allocateDirect(batchSize * numElements * numChannels * dataType.byteSize())
//
//        // Reshape the data by rearranging the bytes
//        for (batch in 0 until batchSize) {
//            for (element in 0 until numElements) {
//                for (channel in 0 until numChannels) {
//                    // Calculate the indices for the original and reshaped buffers
//                    val originalIndex = 0 + channel * numElements + element
//
//                    // Copy the byte from the original buffer to the reshaped buffer
//                    reshapedByteBuffer.putFloat(tensorBuffer.getFloat(originalIndex * Float.SIZE_BYTES))
//                }
//            }
//        }
//
//        // Reset the position of the buffer before returning
//        reshapedByteBuffer.flip()
//
//        return reshapedByteBuffer
//    }

}