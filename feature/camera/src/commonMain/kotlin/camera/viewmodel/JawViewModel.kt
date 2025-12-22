package camera.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import detector.SideDetector.getIncompleteSide
import ext.convertToJawStatus
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import model.FrameAnalyzeStatus
import model.JawSide
import model.JawSideStatus
import model.JawType
import model.ToothDetectionStatus
import model.ToothNumber
import kotlin.collections.set
import kotlin.text.toInt

typealias JawProgress = Map<JawType, Int>

class JawViewModel : ViewModel(){

    private val _upperIllustrationTeeth = MutableStateFlow(
        mapOf(
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
    )
    val upperIllustrationTeeth: StateFlow<Map<ToothNumber, ToothDetectionStatus>> =
        _upperIllustrationTeeth

    private val _lowerIllustrationTeeth = MutableStateFlow(
        mapOf(
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
    )
    val lowerIllustrationTeeth: StateFlow<Map<ToothNumber, ToothDetectionStatus>> =
        _lowerIllustrationTeeth


    private val _frontIllustrationTeeth = MutableStateFlow(
        mapOf(
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
    )
    val frontIllustrationTeeth: StateFlow<Map<ToothNumber, ToothDetectionStatus>> =
        _frontIllustrationTeeth

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

    /**
     * save the status of analyzing for each side
     */
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

    private val _jawsProgressDic = MutableStateFlow(
        mapOf(
            JawType.UPPER to 0,
            JawType.LOWER to 0,
            JawType.FRONT to 0
        )
    )
    val jawsProgressDic = _jawsProgressDic.asStateFlow()

    fun averageJawsProgress(): Int {
        return _jawsProgressDic.value.values.average().toInt()
    }
    init {
        val upperJawTeethCount = 16
        val lowerJawTeethCount = 16
        val frontJawTeethCount = 12

        viewModelScope.launch {
            _upperIllustrationTeeth
                .map { newVal ->
                    val determinedItemCount =
                        newVal.filter { it.value == ToothDetectionStatus.DETECTED }.count()
                    100 * determinedItemCount / upperJawTeethCount
                }
                .collectLatest { progress ->
                    _jawsProgressDic.value = _jawsProgressDic.value.toMutableMap().apply {
                        this[JawType.UPPER] = progress
                    }
                    checkAllJawsCompleted()
                }
        }

        viewModelScope.launch {
            _lowerIllustrationTeeth
                .map { newVal ->
                    val determinedItemCount =
                        newVal.filter { it.value == ToothDetectionStatus.DETECTED }.count()
                    100 * determinedItemCount / lowerJawTeethCount
                }
                .collectLatest { progress ->
                    _jawsProgressDic.value = _jawsProgressDic.value.toMutableMap().apply {
                        this[JawType.LOWER] = progress
                    }
                    checkAllJawsCompleted()
                }
        }

        viewModelScope.launch {
            _frontIllustrationTeeth.map { newVal ->
                val determinedItemCount =
                    newVal.filter { it.value == ToothDetectionStatus.DETECTED }.count()
                100 * determinedItemCount / frontJawTeethCount
            }.collectLatest { progress ->
                _jawsProgressDic.value = _jawsProgressDic.value.toMutableMap().apply {
                    this[JawType.FRONT] = progress
                }
                checkAllJawsCompleted()
            }
        }
    }

    private val _allJawsCompleted = MutableStateFlow(false)
    val allJawsCompleted = _allJawsCompleted.asStateFlow()

    private fun checkAllJawsCompleted() {
        val allCompleted = _jawsProgressDic.value.values.all { it == 100 }
        _allJawsCompleted.value = allCompleted
    }

    private val _currentJawType = MutableStateFlow(JawType.UPPER)
    val currentJawType = _currentJawType.asStateFlow()

    fun changeDetectingJawType(jawType: JawType) {
        _currentJawType.value = jawType
        getIncompleteSide(jawType)?.let { changeDetectingJawSide(it) }
    }

    private val _currentJawSide = MutableStateFlow(JawSide.LEFT)
    val currentJawSide = _currentJawSide.asStateFlow()

    fun changeDetectingJawSide(jawSide: JawSide) {
        _currentJawSide.value = jawSide
        println("current jaw side is:${jawSide}")
    }


    /**
     * Updating the accepted teeth for each jaw
     */
    fun updateAcceptedTeeth(acceptedTeeth: List<ToothNumber>, jawType: JawType, jawSide: JawSide) {

        when (jawType) {
            JawType.UPPER -> {
                when (jawSide) {
                    JawSide.LEFT -> _upperIllustrationTeeth.value =
                        _upperIllustrationTeeth.value.toMutableMap().apply {
                            acceptedTeeth.forEach {
                                if (this.containsKey(it) && upperLeftSide.contains(it)) {
                                    this[it] = ToothDetectionStatus.DETECTED
                                }
                            }
                        }

                    JawSide.RIGHT -> _upperIllustrationTeeth.value =
                        _upperIllustrationTeeth.value.toMutableMap().apply {
                            acceptedTeeth.forEach {
                                if (this.containsKey(it) && upperRightSide.contains(it)) {
                                    this[it] = ToothDetectionStatus.DETECTED
                                }
                            }
                        }

                    JawSide.MIDDLE -> _upperIllustrationTeeth.value =
                        _upperIllustrationTeeth.value.toMutableMap().apply {
                            acceptedTeeth.forEach {
                                if (this.containsKey(it) && upperMiddleSide.contains(it)) {
                                    this[it] = ToothDetectionStatus.DETECTED
                                }
                            }
                        }
                }

            }

            JawType.LOWER -> {
                when (jawSide) {
                    JawSide.LEFT -> _lowerIllustrationTeeth.value =
                        _lowerIllustrationTeeth.value.toMutableMap().apply {
                            acceptedTeeth.forEach {
                                if (this.containsKey(it) && lowerLeftSide.contains(it)) {
                                    this[it] = ToothDetectionStatus.DETECTED
                                }
                            }
                        }

                    JawSide.RIGHT -> _lowerIllustrationTeeth.value =
                        _lowerIllustrationTeeth.value.toMutableMap().apply {
                            acceptedTeeth.forEach {
                                if (this.containsKey(it) && lowerRightSide.contains(it)) {
                                    this[it] = ToothDetectionStatus.DETECTED
                                }
                            }
                        }

                    JawSide.MIDDLE -> _lowerIllustrationTeeth.value =
                        _lowerIllustrationTeeth.value.toMutableMap().apply {
                            acceptedTeeth.forEach {
                                if (this.containsKey(it) && lowerMiddleSide.contains(it)) {
                                    this[it] = ToothDetectionStatus.DETECTED
                                }
                            }
                        }
                }
            }

            JawType.FRONT -> {
                _frontIllustrationTeeth.value = _frontIllustrationTeeth.value.toMutableMap().apply {
                    acceptedTeeth.forEach {
                        if (this.containsKey(it) && frontSide.contains(it)) {
                            this[it] = ToothDetectionStatus.DETECTED
                        }
                    }
                }
            }
        }

        getIncompleteSide(jawType)?.let { changeDetectingJawSide(it) }
    }
}