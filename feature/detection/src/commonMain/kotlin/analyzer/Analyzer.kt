package analyzer

import androidx.compose.ui.graphics.ImageBitmap
import detector.calculateNormalizedPadding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.channels.produce
import model.JawSide
import model.JawType
import model.ToothBox
import postprocessing.FrontNumbering
import postprocessing.OutputProcessing
import postprocessing.UpperLowerNumbering

const val IMAGE_TYPE_UPPER = "upper"
const val IMAGE_TYPE_LOWER = "lower"

@OptIn(ExperimentalCoroutinesApi::class)
fun CoroutineScope.processNormalizedPadding(resizedBitmap: ImageBitmap) = produce {
    send(resizedBitmap.calculateNormalizedPadding())
}

@OptIn(ExperimentalCoroutinesApi::class)
fun CoroutineScope.processFrontNumbering(
    normalizedToothBoxes: List<ToothBox>,
    normalizedPadding: Float
) = produce {
    send(FrontNumbering.processNumberingForFront(normalizedToothBoxes, normalizedPadding))
}

@OptIn(ExperimentalCoroutinesApi::class)
fun CoroutineScope.processUpperNumbering(
    normalizedToothBoxes: List<ToothBox>,
    normalizedPadding: Float
) = produce {
    send(
        UpperLowerNumbering.numberingV3(
            boxes = normalizedToothBoxes,
            imgType = IMAGE_TYPE_UPPER,
            normalizedPadding = normalizedPadding
        )
    )
}

@OptIn(ExperimentalCoroutinesApi::class)
fun CoroutineScope.processLowerNumbering(
    normalizedToothBoxes: List<ToothBox>,
    normalizedPadding: Float
) = produce {
    send(
        UpperLowerNumbering.numberingV3(
            boxes = normalizedToothBoxes,
            imgType = IMAGE_TYPE_UPPER,
            normalizedPadding = normalizedPadding
        )
    )
}

@OptIn(ExperimentalCoroutinesApi::class)
fun CoroutineScope.deviceDistanceChecker(
    visibleBoxesCount: Int,
    missedCount: Int,
    jawSide: JawSide,
    jawType: JawType,
    onMoveCloser: () -> Unit,
    onMoveMuchCloser: () -> Unit,
    onMoveAway: () -> Unit,
    onMoveMuchAway: () -> Unit
) = produce {
    send(
        OutputProcessing.calculateDistanceBetweenDeviceAndTooth(
            visibleBoxesCount,
            missedCount,
            jawSide,
            jawType,
            onMoveCloser,
            onMoveMuchCloser,
            onMoveAway,
            onMoveMuchAway,
        )
    )
}

@OptIn(ExperimentalCoroutinesApi::class)
fun CoroutineScope.deviceAngelChecker(
    visibleBoxes:List<ToothBox>,
    jawSide: JawSide,
    jawType: JawType
) = produce {
    MotionListener.registerMotionListener()
    send(deviceAngelChecker(visibleBoxes, jawType, jawSide))
    MotionListener.unregisterMotionListener()
}