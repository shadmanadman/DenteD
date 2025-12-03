package camera.viewmodel

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import litert.Litert
import model.JawType
import postprocessing.NumberingResult

private const val SEGMENT_UPPER_MODEL_NAME = "detection_upper_float32.tflite"
private const val SEGMENT_LOWER_MODEL_NAME = "detection_lower_float32.tflite"
private const val SEGMENT_FRONT_MODEL_NAME = "detection_front_float32.tflite"
private const val NORMALIZED_PADDING = 80f

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

    private val _normalizedPadding = MutableStateFlow(NORMALIZED_PADDING)

    fun getNormalizedPadding(): Float {
        return _normalizedPadding.value
    }

    fun setNormalizedPadding(padding:Float){
        _normalizedPadding.value = padding
    }


}