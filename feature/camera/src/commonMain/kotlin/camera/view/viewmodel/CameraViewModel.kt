package camera.viewmodel

import androidx.compose.runtime.mutableDoubleStateOf
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import shared.model.AcceptedFrame
import shared.model.DetectingStatus
import shared.model.FocusSection
import shared.model.ToothNumber
import postprocessing.NumberingResult


class CameraViewModel : ViewModel(){
    private val _detectingStatus = MutableStateFlow(DetectingStatus.Started)

    fun stopDetecting() {
        _detectingStatus.value = DetectingStatus.Stopped
    }

    fun startDetecting() {
        _detectingStatus.value = DetectingStatus.Started
    }

    fun getDetectionStatus():DetectingStatus{
        return _detectingStatus.value
    }

    private val _currentFocusSection = MutableStateFlow(FocusSection.MIDDLE)

    fun changeFocusedSection(focusedSection: FocusSection) {
        _currentFocusSection.value = focusedSection
    }

    fun getCurrentFocusSection(): FocusSection {
        return _currentFocusSection.value
    }


    /**
     * Analyze focused frame and find acceptable tooth. if the frame contains at least one
     * clear tooth we hold and save the frame.
     */
    private val _acceptedTeeth = MutableStateFlow(mutableListOf<ToothNumber>())
    val acceptedTeeth = _acceptedTeeth.asStateFlow()

    private val _acceptedTeethTemp = MutableStateFlow(mutableListOf<ToothNumber>())

    private val _acceptedFrames = MutableStateFlow(AcceptedFrame())
    val acceptedFrames = _acceptedFrames.asStateFlow()

    private val calibratedClarityLevel = mutableDoubleStateOf(0.0)

    private fun addAcceptedTeethToTemp(toothNumbers: List<ToothNumber>) {
        _acceptedTeethTemp.update { currentList ->
            currentList.toMutableList().apply {
                addAll(toothNumbers.filter { it !in currentList })
            }
        }
        println("This are the accepted tooth:$toothNumbers")
    }

    private fun addAcceptedTooth() {
        _acceptedTeeth.update { currentList ->
            currentList.toMutableList().apply {
                addAll(_acceptedTeethTemp.value.filter { it !in currentList })
            }
        }
    }




    private val _numberingResult = MutableStateFlow(NumberingResult())
    val numberingResult = _numberingResult.asStateFlow()

    private val _normalizedPadding = MutableStateFlow(80f)
    val normalizedPadding = _normalizedPadding.asStateFlow()
}