package detector

import androidx.compose.ui.graphics.ImageBitmap
import ext.resize
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.channels.ReceiveChannel
import kotlinx.coroutines.channels.produce
import litert.Litert
import litert.ModelOutputReshaped
import litert.toScaledByteBuffer
import model.Box
import model.JawType
import model.ToothBox

const val INPUT_IMAGE_SIZE = 640F
const val IOU_THRESHOLD = 0.50F
const val BATCH_SIZE = 1
const val PIXEL_SIZE = 3
const val BYTES_PER_CHANNEL = 4
const val MODEL_INPUT_ALLOCATED_SIZE =
    BATCH_SIZE * INPUT_IMAGE_SIZE * INPUT_IMAGE_SIZE * PIXEL_SIZE * BYTES_PER_CHANNEL

val outputContainer: ModelOutputReshaped = Array(BATCH_SIZE) {
    Array(NUM_ELEMENTS) { FloatArray(NUM_CHANNELS) }
}
const val NUM_CHANNELS = 40
const val NUM_ELEMENTS = 8400
const val CLASS_SCORE_START_INDEX = 4
const val CLASS_SCORE_END_INDEX_FRONT_JAW = 12

const val X_INDEX = 0
const val Y_INDEX = 1
const val W_INDEX = 2
const val H_INDEX = 3



@OptIn(ExperimentalCoroutinesApi::class)
fun CoroutineScope.runDetection(
    inputs: List<ByteArray>,
    model: Litert,
    isFrontJaw: Boolean,
): ReceiveChannel<List<ToothBox>> = produce {
    model.run(inputs, mapOf(Pair(0, outputContainer)))
    val output = processOutput(isFrontJaw, outputContainer).receive()
    send(output)
}

@OptIn(ExperimentalCoroutinesApi::class)
fun CoroutineScope.modelOutputLooper(modelOutput: ModelOutputReshaped) = produce {
    val modelOutput = modelOutput
    for (i in 0 until NUM_ELEMENTS) {
        send(modelOutput[0][i])
    }
}

@OptIn(ExperimentalCoroutinesApi::class)
fun CoroutineScope.classScoreExtractor(
    isFrontJaw: Boolean,
    modelOutput: ReceiveChannel<FloatArray>
) = produce {
    val modelOutput = modelOutput.receive()
    // The first 4 elements are box coordinates (x, y, w, h). Class scores start at index 4.
    val classScores = if (isFrontJaw) {
        // For the front jaw, we consider 8 classes (scores at indices 4 through 11).
        modelOutput.slice(CLASS_SCORE_START_INDEX until CLASS_SCORE_END_INDEX_FRONT_JAW)
    } else {
        // For a side jaw, we only consider the first 4 classes (scores at indices 4 through 7).
        modelOutput.slice(CLASS_SCORE_START_INDEX until CLASS_SCORE_START_INDEX + 4)
    }
    send(classScores)
}

@OptIn(ExperimentalCoroutinesApi::class)
fun CoroutineScope.normalizeDetectedBox(modelOutput: ReceiveChannel<FloatArray>) = produce<Box> {
    val modelOutput = modelOutput.receive()
    val normalizeConstant: Float = INPUT_IMAGE_SIZE

    val normalizedX = modelOutput[X_INDEX] / normalizeConstant
    val normalizedY = modelOutput[Y_INDEX] / normalizeConstant
    val normalizedWidth = modelOutput[W_INDEX] / normalizeConstant
    val normalizedHeight = modelOutput[H_INDEX] / normalizeConstant
    send(Box(normalizedX, normalizedY, normalizedWidth, normalizedHeight))
}

@OptIn(ExperimentalCoroutinesApi::class)
fun CoroutineScope.validToothBoxExtractor(
    maxScore: Float,
    maxScoreIndex: Int,
    modelOutput: ReceiveChannel<FloatArray>
) = produce {
    val validRows = mutableListOf<ToothBox>()

    // Check if the maximum score is above the threshold
    if (maxScore > IOU_THRESHOLD) {
        println(maxScore)

        val normalizedBox = normalizeDetectedBox(modelOutput).receive()

        validRows.add(
            ToothBox(
                category = maxScoreIndex,
                x = normalizedBox.x,
                y = normalizedBox.y,
                width = normalizedBox.width,
                height = normalizedBox.height,
                maxConf = maxScore,
                number = 0,
                alphabeticNumber = ""
            )
        )
    }
    println("Finish detecting process. fount ${validRows.size} valid rows")

    send(validRows)
}


@OptIn(ExperimentalCoroutinesApi::class)
fun CoroutineScope.processOutput(
    isFrontJaw: Boolean,
    modelOutputReceiver: ModelOutputReshaped
) =
    produce<List<ToothBox>> {
        val modelOutputLooper = modelOutputLooper(modelOutputReceiver)
        val classScores = classScoreExtractor(isFrontJaw, modelOutputLooper).receive()

        val maxScoreIndex = classScores.indexOf(classScores.maxOrNull())
        val maxScore = classScores[maxScoreIndex]

        send(validToothBoxExtractor(maxScore, maxScoreIndex, modelOutputLooper).receive())
    }



@OptIn(ExperimentalCoroutinesApi::class)
fun CoroutineScope.detectCurrentSide(
    jawType: JawType,
    visibleBox: List<ToothBox>,
    missing: List<String>,
) = produce {
    val visibleBox = visibleBox.map { it.alphabeticNumber }
        .toSet()
    send(
        SideDetector.detectCurrentSide(visibleBox,missing,jawType)
    )
}



