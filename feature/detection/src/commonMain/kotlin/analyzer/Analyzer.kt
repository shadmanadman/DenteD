package analyzer

import androidx.compose.ui.graphics.ImageBitmap
import androidx.lifecycle.ViewModel
import detector.calculateNormalizedPadding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.channels.produce
import shared.model.JawSide
import shared.model.JawType
import shared.model.ToothBox
import postprocessing.FrontNumbering
import postprocessing.OutputProcessing
import postprocessing.UpperLowerNumbering

const val IMAGE_TYPE_UPPER = "upper"
const val IMAGE_TYPE_LOWER = "lower"

object Analyzer {

    @OptIn(ExperimentalCoroutinesApi::class)
    fun CoroutineScope.processNumbering(
        normalizedToothBoxes: List<ToothBox>,
        normalizedPadding: Float,
        jawType: JawType
    ) = produce {
        when (jawType) {
            JawType.UPPER -> send(
                processUpperNumbering(normalizedToothBoxes, normalizedPadding).receive()
            )

            JawType.LOWER -> send(
                processLowerNumbering(normalizedToothBoxes, normalizedPadding).receive()
            )

            JawType.FRONT -> send(
                processFrontNumbering(normalizedToothBoxes, normalizedPadding).receive()
            )
        }
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    private fun CoroutineScope.processFrontNumbering(
        normalizedToothBoxes: List<ToothBox>,
        normalizedPadding: Float
    ) = produce {
        send(FrontNumbering.processNumberingForFront(normalizedToothBoxes, normalizedPadding))
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    private fun CoroutineScope.processUpperNumbering(
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
    private fun CoroutineScope.processLowerNumbering(
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
        visibleBoxes: List<ToothBox>,
        jawSide: JawSide,
        jawType: JawType
    ) = produce {
        MotionListener.registerMotionListener()
        send(deviceAngelChecker(visibleBoxes, jawType, jawSide))
        MotionListener.unregisterMotionListener()
    }

}