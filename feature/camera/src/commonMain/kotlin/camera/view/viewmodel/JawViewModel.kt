package camera.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import camera.view.contract.JawUiState
import detector.SideDetector.getIncompleteSide
import shared.ext.convertToJawStatus
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import shared.model.FrameAnalyzeStatus
import shared.model.JawSide
import shared.model.JawSideStatus
import shared.model.JawType
import shared.model.ToothDetectionStatus
import shared.model.ToothNumber
import kotlin.collections.set

typealias JawProgress = Map<JawType, Int>

class JawViewModel : ViewModel() {
    private val _uiState = MutableStateFlow(JawUiState())
    val uiState: StateFlow<JawUiState> = _uiState.asStateFlow()

    init {
        _uiState.value.upperIllustrationTeeth = mapOf(
            ToothNumber.UL8 to ToothDetectionStatus.INITIAL,
            ToothNumber.UL7 to ToothDetectionStatus.INITIAL,
            ToothNumber.UL6 to ToothDetectionStatus.INITIAL,
            ToothNumber.UL5 to ToothDetectionStatus.INITIAL,
            ToothNumber.UL4 to ToothDetectionStatus.INITIAL,
            ToothNumber.UL3 to ToothDetectionStatus.INITIAL,
            ToothNumber.UL2 to ToothDetectionStatus.INITIAL,
            ToothNumber.UL1 to ToothDetectionStatus.INITIAL,
            ToothNumber.UR1 to ToothDetectionStatus.INITIAL,
            ToothNumber.UR2 to ToothDetectionStatus.INITIAL,
            ToothNumber.UR3 to ToothDetectionStatus.INITIAL,
            ToothNumber.UR4 to ToothDetectionStatus.INITIAL,
            ToothNumber.UR5 to ToothDetectionStatus.INITIAL,
            ToothNumber.UR6 to ToothDetectionStatus.INITIAL,
            ToothNumber.UR7 to ToothDetectionStatus.INITIAL,
            ToothNumber.UR8 to ToothDetectionStatus.INITIAL
        )


        _uiState.value.lowerIllustrationTeeth = mapOf(
            ToothNumber.LL8 to ToothDetectionStatus.INITIAL,
            ToothNumber.LL7 to ToothDetectionStatus.INITIAL,
            ToothNumber.LL6 to ToothDetectionStatus.INITIAL,
            ToothNumber.LL5 to ToothDetectionStatus.INITIAL,
            ToothNumber.LL4 to ToothDetectionStatus.INITIAL,
            ToothNumber.LL3 to ToothDetectionStatus.INITIAL,
            ToothNumber.LL2 to ToothDetectionStatus.INITIAL,
            ToothNumber.LL1 to ToothDetectionStatus.INITIAL,
            ToothNumber.LR1 to ToothDetectionStatus.INITIAL,
            ToothNumber.LR2 to ToothDetectionStatus.INITIAL,
            ToothNumber.LR3 to ToothDetectionStatus.INITIAL,
            ToothNumber.LR4 to ToothDetectionStatus.INITIAL,
            ToothNumber.LR5 to ToothDetectionStatus.INITIAL,
            ToothNumber.LR6 to ToothDetectionStatus.INITIAL,
            ToothNumber.LR7 to ToothDetectionStatus.INITIAL,
            ToothNumber.LR8 to ToothDetectionStatus.INITIAL
        )

        _uiState.value.frontIllustrationTeeth = mapOf(
            ToothNumber.LR3 to ToothDetectionStatus.INITIAL,
            ToothNumber.LR2 to ToothDetectionStatus.INITIAL,
            ToothNumber.LR1 to ToothDetectionStatus.INITIAL,
            ToothNumber.LL1 to ToothDetectionStatus.INITIAL,
            ToothNumber.LL2 to ToothDetectionStatus.INITIAL,
            ToothNumber.LL3 to ToothDetectionStatus.INITIAL,
            ToothNumber.UR3 to ToothDetectionStatus.INITIAL,
            ToothNumber.UR2 to ToothDetectionStatus.INITIAL,
            ToothNumber.UR1 to ToothDetectionStatus.INITIAL,
            ToothNumber.UL1 to ToothDetectionStatus.INITIAL,
            ToothNumber.UL2 to ToothDetectionStatus.INITIAL,
            ToothNumber.UL3 to ToothDetectionStatus.INITIAL,
        )
    }

    private val lowerLeftSide = listOf(
        ToothNumber.LL8,
        ToothNumber.LL7,
        ToothNumber.LL6,
        ToothNumber.LL5,
        ToothNumber.LL4
    )
    private val lowerRightSide = listOf(
        ToothNumber.LR8,
        ToothNumber.LR7,
        ToothNumber.LR6,
        ToothNumber.LR5,
        ToothNumber.LR4
    )

    private val lowerMiddleSide = listOf(
        ToothNumber.LL3,
        ToothNumber.LL2,
        ToothNumber.LL1,
        ToothNumber.LR1,
        ToothNumber.LR2,
        ToothNumber.LR3
    )

    private val upperMiddleSide = listOf(
        ToothNumber.UL3,
        ToothNumber.UL2,
        ToothNumber.UL1,
        ToothNumber.UR1,
        ToothNumber.UR2,
        ToothNumber.UR3
    )

    private val upperRightSide = listOf(
        ToothNumber.UR8,
        ToothNumber.UR7,
        ToothNumber.UR6,
        ToothNumber.UR5,
        ToothNumber.UR4
    )

    private val upperLeftSide = listOf(
        ToothNumber.UL8,
        ToothNumber.UL7,
        ToothNumber.UL6,
        ToothNumber.UL5,
        ToothNumber.UL4
    )

    private val frontSide = listOf(
        ToothNumber.LR3,
        ToothNumber.LR2,
        ToothNumber.LR1,
        ToothNumber.LL1,
        ToothNumber.LL2,
        ToothNumber.LL3,
        ToothNumber.UR3,
        ToothNumber.UR2,
        ToothNumber.UR1,
        ToothNumber.UL1,
        ToothNumber.UL2,
        ToothNumber.UL3
    )

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