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

    fun changeFocusedSection(focusedSection: FocusSection) {
        _uiState.update { it.copy(currentFocusSection = focusedSection) }
    }

    /**
     * Analyze focused frame and find acceptable tooth. if the frame contains at least one
     * clear tooth we hold and save the frame.
     */

    private val acceptedTeethTemp = mutableListOf<ToothNumber>()

    private fun addAcceptedTeethToTemp(toothNumbers: List<ToothNumber>) {
        acceptedTeethTemp.addAll(toothNumbers.filter { it !in acceptedTeethTemp })
        println("This are the accepted tooth:$toothNumbers")
    }

    private fun updateAcceptedTooth() {
        _uiState.update { uiState ->
            uiState.copy(
                acceptedTeeth = uiState.acceptedTeeth.toMutableList().apply {
                    addAll(acceptedTeethTemp.filter { it !in uiState.acceptedTeeth })
                }
            )
        }
    }

    private val calibratedClarityLevel = mutableDoubleStateOf(0.0)


    @OptIn(ExperimentalCoroutinesApi::class)
    private fun processNormalizedPadding(resizedBitmap: ImageBitmap) {
        viewModelScope.launch {
            normalizedPadding = resizedBitmap.calculateNormalizedPadding()
        }
    }

    private val _normalizedPadding = MutableStateFlow(80f)
    val normalizedPadding = _normalizedPadding.asStateFlow()
}