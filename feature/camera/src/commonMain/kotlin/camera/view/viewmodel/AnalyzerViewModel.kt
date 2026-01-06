package camera.viewmodel

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import litert.Litert
import postprocessing.NumberingResult
import shared.ext.convertToJawStatus
import shared.model.FocusSection
import shared.model.FrameAnalyzeStatus
import shared.model.JawSide
import shared.model.JawSideStatus
import shared.model.JawType

private const val SEGMENT_UPPER_MODEL_NAME = "detection_upper_float32.tflite"
private const val SEGMENT_LOWER_MODEL_NAME = "detection_lower_float32.tflite"
private const val SEGMENT_FRONT_MODEL_NAME = "detection_front_float32.tflite"

class AnalyzerViewModel : ViewModel() {
    private val detector = lazy { Litert }.value

    fun initDetector(jawType: JawType,model: ByteArray) {
        detector.init(model)
    }

    fun JawType.toModelName(): String{
        return when (this) {
            JawType.UPPER -> SEGMENT_UPPER_MODEL_NAME
            JawType.LOWER -> SEGMENT_LOWER_MODEL_NAME
            else -> SEGMENT_FRONT_MODEL_NAME
        }
    }


    private val _numberingResult = MutableStateFlow(NumberingResult())

    fun getLatestNumberingResult(): NumberingResult {
        return _numberingResult.value
    }

    fun updateNumberingResult(numberingResult: NumberingResult){
        _numberingResult.value = numberingResult
    }

    //save the status of analyzing for each side
    private val _jawSideStatus = MutableStateFlow(
        mapOf(
            JawSideStatus.LOWER_LEFT to FrameAnalyzeStatus.None,
            JawSideStatus.LOWER_RIGHT to FrameAnalyzeStatus.None,
            JawSideStatus.LOWER_MIDDLE to FrameAnalyzeStatus.None,
            JawSideStatus.UPPER_LEFT to FrameAnalyzeStatus.None,
            JawSideStatus.UPPER_RIGHT to FrameAnalyzeStatus.None,
            JawSideStatus.UPPER_MIDDLE to FrameAnalyzeStatus.None,
            JawSideStatus.FRONT to FrameAnalyzeStatus.None,
        )
    )

    fun jawSideAnalyzeStarted(currentJawSide: JawSideStatus) {
        println("Jaw side analyze started: $currentJawSide")
        _jawSideStatus.value = _jawSideStatus.value.toMutableMap().apply {
            this[currentJawSide] = FrameAnalyzeStatus.Started
        }
    }

    fun jawSideAnalyzeCompleted(currentJawSide: JawSideStatus) {
        println("Jaw side analyze completed: $currentJawSide")
        _jawSideStatus.value = _jawSideStatus.value.toMutableMap().apply {
            this[currentJawSide] = FrameAnalyzeStatus.Completed
        }
    }


    fun checkIfCurrentSideAnalyzeCompleted(jawSide: JawSide, jawType: JawType): Boolean {
        val jawSideStatus = convertToJawStatus(jawSide, jawType)
        return _jawSideStatus.value[jawSideStatus] == FrameAnalyzeStatus.Completed
    }


    private val _currentFocusSection = MutableStateFlow(FocusSection.MIDDLE)
}