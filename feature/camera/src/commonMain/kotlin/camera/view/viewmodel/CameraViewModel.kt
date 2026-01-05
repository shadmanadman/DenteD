package camera.viewmodel

import analyzer.Analyzer.deviceAngelChecker
import analyzer.Analyzer.deviceDistanceChecker
import analyzer.Analyzer.processNumbering
import androidx.compose.runtime.mutableDoubleStateOf
import androidx.compose.ui.graphics.ImageBitmap
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import camera.view.contract.CameraUiState
import detector.Detector.detectCurrentSide
import detector.Detector.runDetection
import detector.calculateNormalizedPadding
import detector.squareMe
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import shared.model.DetectingStatus
import shared.model.FocusSection
import shared.model.ToothNumber
import shared.ext.resize
import shared.model.CameraErrorState
import shared.model.JawSide
import shared.model.JawType
import shared.model.ToothBox
import shared.platform.SharedImage


class CameraViewModel : ViewModel() {
    private val _uiState = MutableStateFlow(CameraUiState())
    val uiState: StateFlow<CameraUiState> = _uiState.asStateFlow()

    fun stopDetecting() {
        _uiState.update { it.copy(detectionStatus = DetectingStatus.Stopped) }
    }

    fun startDetecting() {
        _uiState.update { it.copy(detectionStatus = DetectingStatus.Started) }
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

    private var normalizedPadding = 0f

    @OptIn(ExperimentalCoroutinesApi::class)
    private fun processNormalizedPadding(resizedBitmap: ImageBitmap) {
        viewModelScope.launch {
            normalizedPadding = resizedBitmap.calculateNormalizedPadding()
        }
    }

    fun startImageAnalysis(
        inputImage: SharedImage,
        jawType: JawType = JawType.UPPER,
        jawSide: JawSide = JawSide.LEFT
    ) {
        viewModelScope.launch {
            var normalizedToothBox = listOf<ToothBox>()

            if (_uiState.value.detectionStatus == DetectingStatus.Stopped) return@launch

            // Resize input for model
            val resizedInput = inputImage.toImageBitmap()?.squareMe()?.resize()

            // Calculate normalized padding
            if (normalizedPadding == 0f && resizedInput != null) processNormalizedPadding(
                resizedInput
            )

            // Run detection
            if (resizedInput != null)
                normalizedToothBox =
                    this.runDetection(input = resizedInput, isFrontJaw = jawType == JawType.FRONT)
                        .receive()


            if (normalizedToothBox.isEmpty()) {
                println("No tooth detected")
                return@launch
            }

            // Run numbering calculation
            val numberingResult =
                this.processNumbering(normalizedToothBox, normalizedPadding, jawType).receive()

            _uiState.update { it.copy(numberingResult = numberingResult) }

            val visibleToothBoxes = _uiState.value.numberingResult.visibleBoxes
            val missingTooth = _uiState.value.numberingResult.missing

            if (visibleToothBoxes.isEmpty()) {
                println("No visible tooth detected")
                return@launch
            }

            println("Numbering result: ${visibleToothBoxes.map { it.alphabeticNumber }}")

            // Detect current side
            val currentSide =
                this.detectCurrentSide(jawType, visibleToothBoxes, missingTooth).receive()

            if (currentSide != jawSide) {
                println("Wrong side detected")
                return@launch
            }

            // Check device distance
            val deviceDistanceChecker = this.deviceDistanceChecker(
                visibleBoxesCount = visibleToothBoxes.count(),
                missedCount = missingTooth.count(),
                jawSide = jawSide,
                jawType = jawType,
                onMoveCloser = { _uiState.update { it.copy(detectionErrorState = CameraErrorState.MoveCloser) } },
                onMoveMuchCloser = { _uiState.update { it.copy(detectionErrorState = CameraErrorState.MoveMuchCloser) } },
                onMoveAway = { _uiState.update { it.copy(detectionErrorState = CameraErrorState.MoveAway) } },
                onMoveMuchAway = { _uiState.update { it.copy(detectionErrorState = CameraErrorState.MoveMuchAway) } }
            ).receive()

            if (deviceDistanceChecker) {
                println("Wrong distance detected")
                return@launch
            }


            // Check device angle
            val deviceAngleChecker =
                this.deviceAngelChecker(visibleToothBoxes, jawSide, jawType).receive()

            if (deviceAngleChecker) {
                _uiState.update { it.copy(detectionErrorState = CameraErrorState.BadAngel) }
                println("Wrong angle detected")
                return@launch
            }


            // Everything is ok to focus, zoom and manage accepted tooth
            _uiState.update { it.copy(detectionErrorState = CameraErrorState.Ok) }
        }

    }


}