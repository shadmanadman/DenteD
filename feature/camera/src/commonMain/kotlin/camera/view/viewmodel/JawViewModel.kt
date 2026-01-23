package camera.view.viewmodel

import androidx.lifecycle.ViewModel
import camera.view.contract.JawUiState
import detector.SideDetector.getIncompleteSide
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import shared.ext.convertToJawStatus
import shared.ext.frontSide
import shared.ext.lowerLeftSide
import shared.ext.lowerMiddleSide
import shared.ext.lowerRightSide
import shared.ext.toJawType
import shared.ext.upperLeftSide
import shared.ext.upperMiddleSide
import shared.ext.upperRightSide
import shared.model.FrameAnalyzeStatus
import shared.model.JawSide
import shared.model.JawSideStatus
import shared.model.JawType
import shared.model.ToothDetectionStatus
import shared.model.ToothNumber

class JawViewModel : ViewModel() {
    private val _uiState = MutableStateFlow(JawUiState())
    val uiState: StateFlow<JawUiState> = _uiState.asStateFlow()

    fun applySelection(selection: List<ToothNumber>) {
        if (selection.isEmpty()) return
        _uiState.update { uiState ->
            uiState.copy(
                selectedJaw = selection.toJawType(),
                upperIllustrationTeeth = uiState.upperIllustrationTeeth.mapValues { (number, status) ->
                    if (selection.contains(number)) status else ToothDetectionStatus.DISABLED
                },
                lowerIllustrationTeeth = uiState.lowerIllustrationTeeth.mapValues { (number, status) ->
                    if (selection.contains(number)) status else ToothDetectionStatus.DISABLED
                },
                frontIllustrationTeeth = uiState.frontIllustrationTeeth.mapValues { (number, status) ->
                    if (selection.contains(number)) status else ToothDetectionStatus.DISABLED
                }
            )
        }
    }

    fun jawSideAnalyzeStarted(currentJawSide: JawSideStatus) {
        println("Jaw side analyze started: $currentJawSide")
        _uiState.update {
            it.copy(jawSideAnalyzeState = it.jawSideAnalyzeState.toMutableMap().apply {
                this[currentJawSide] = FrameAnalyzeStatus.Started
            })
        }
    }

    fun jawSideAnalyzeCompleted(currentJawSide: JawSideStatus) {
        println("Jaw side analyze completed: $currentJawSide")
        _uiState.update {
            it.copy(jawSideAnalyzeState = it.jawSideAnalyzeState.toMutableMap().apply {
                this[currentJawSide] = FrameAnalyzeStatus.Completed
            })
        }
    }

    fun checkIfCurrentSideAnalyzeCompleted(jawSide: JawSide, jawType: JawType): Boolean {
        val jawSideStatus = convertToJawStatus(jawSide, jawType)
        return _uiState.value.jawSideAnalyzeState[jawSideStatus] == FrameAnalyzeStatus.Completed
    }

    fun changeDetectingJawType(jawType: JawType) {
        _uiState.update { it.copy(jawType = jawType) }
        getIncompleteSide(jawType)?.let { changeDetectingJawSide(it) }
    }

    fun changeDetectingJawSide(jawSide: JawSide) {
        _uiState.update { it.copy(jawSide = jawSide) }
        println("current jaw side is:${jawSide}")
    }


    /**
     * Updating the accepted teeth for each jaw
     */
    fun updateAcceptedTeeth(acceptedTeeth: List<ToothNumber>, jawType: JawType, jawSide: JawSide) {
        val upperJawTeethCount = 16
        val lowerJawTeethCount = 16
        val frontJawTeethCount = 12

        _uiState.update { state ->

            fun Map<ToothNumber, ToothDetectionStatus>.applyAccepted(
                allowed: List<ToothNumber>
            ): Map<ToothNumber, ToothDetectionStatus> =
                toMutableMap().apply {
                    acceptedTeeth.forEach { tooth ->
                        if (containsKey(tooth) && allowed.contains(tooth)) {
                            this[tooth] = ToothDetectionStatus.DETECTED
                        }
                    }
                }

            val updatedState = when (jawType) {

                JawType.UPPER -> state.copy(
                    upperIllustrationTeeth = state.upperIllustrationTeeth.applyAccepted(
                        when (jawSide) {
                            JawSide.LEFT -> upperLeftSide
                            JawSide.RIGHT -> upperRightSide
                            JawSide.MIDDLE -> upperMiddleSide
                        }
                    )
                )

                JawType.LOWER -> state.copy(
                    lowerIllustrationTeeth = state.lowerIllustrationTeeth.applyAccepted(
                        when (jawSide) {
                            JawSide.LEFT -> lowerLeftSide
                            JawSide.RIGHT -> lowerRightSide
                            JawSide.MIDDLE -> lowerMiddleSide
                        }
                    )
                )

                JawType.FRONT -> state.copy(
                    frontIllustrationTeeth = state.frontIllustrationTeeth.applyAccepted(
                        frontSide
                    )
                )
            }

            val progress = mapOf(
                JawType.UPPER to calculateProgress(
                    updatedState.upperIllustrationTeeth,
                    upperJawTeethCount
                ),
                JawType.LOWER to calculateProgress(
                    updatedState.lowerIllustrationTeeth,
                    lowerJawTeethCount
                ),
                JawType.FRONT to calculateProgress(
                    updatedState.frontIllustrationTeeth,
                    frontJawTeethCount
                )
            )

            updatedState.copy(
                jawProgress = progress,
                allJawsCompleted = progress.values.all { it == 100 },
                averageJawsProgress = progress.values.average().toInt()
            )
        }

        getIncompleteSide(jawType)?.let { changeDetectingJawSide(it) }
    }

    private fun calculateProgress(
        teeth: Map<ToothNumber, ToothDetectionStatus>,
        totalCount: Int
    ): Int {
        val detected = teeth.count { it.value == ToothDetectionStatus.DETECTED }
        return if (totalCount == 0) 0 else (100 * detected / totalCount)
    }
}