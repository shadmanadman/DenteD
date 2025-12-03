package detector

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import model.JawSide
import model.JawType
import model.ToothDetectionStatus
import model.ToothNumber
object SideDetector {

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


    fun getIncompleteSide(type: JawType): JawSide? {
        return when (type) {
            JawType.FRONT -> {
                if (!isSideScanCompleted(JawSide.MIDDLE, type)) {
                    JawSide.MIDDLE
                } else {
                    null
                }
            }

            JawType.LOWER, JawType.UPPER -> {
                if (!isSideScanCompleted(JawSide.LEFT, type)) {
                    JawSide.LEFT
                } else if (!isSideScanCompleted(JawSide.MIDDLE, type)) {
                    JawSide.MIDDLE
                } else if (!isSideScanCompleted(JawSide.RIGHT, type)) {
                    JawSide.RIGHT
                } else {
                    null
                }
            }

        }
    }

    fun isSideScanCompleted(side: JawSide, type: JawType): Boolean {
        return when (type) {
            JawType.LOWER -> {
                when (side) {
                    JawSide.LEFT -> {
                        checkCompletion(lowerLeftSide, lowerIllustrationTeeth.value)
                    }

                    JawSide.MIDDLE -> {
                        checkCompletion(lowerMiddleSide, lowerIllustrationTeeth.value)
                    }

                    JawSide.RIGHT -> {
                        checkCompletion(lowerRightSide, lowerIllustrationTeeth.value)
                    }
                }
            }

            JawType.UPPER -> {
                when (side) {
                    JawSide.LEFT -> {
                        checkCompletion(upperLeftSide, upperIllustrationTeeth.value)
                    }

                    JawSide.MIDDLE -> {
                        checkCompletion(upperMiddleSide, upperIllustrationTeeth.value)
                    }

                    JawSide.RIGHT -> {
                        checkCompletion(upperRightSide, upperIllustrationTeeth.value)
                    }
                }
            }

            JawType.FRONT -> {
                when (side) {
                    JawSide.LEFT -> true
                    JawSide.MIDDLE -> {
                        // Accept if at least one tooth from upper and one tooth from lower appears
                        _frontIllustrationTeeth.value.any {
                            it.value != ToothDetectionStatus.INITIAL && it.key.name.startsWith(
                                "L"
                            )
                        } &&
                                frontIllustrationTeeth.value.any {
                                    it.value != ToothDetectionStatus.INITIAL && it.key.name.startsWith(
                                        "U"
                                    )
                                }
                    }

                    JawSide.RIGHT -> true
                }
            }
        }
    }

    fun checkCompletion(
        teeth: List<ToothNumber>,
        toothMap: Map<ToothNumber, ToothDetectionStatus>,
        completionThreshold: Double = 0.8
    ): Boolean {
        val detectedTeethCount = teeth.count { toothMap[it] == ToothDetectionStatus.DETECTED }
        val totalTeethCount = teeth.size
        return detectedTeethCount.toDouble() / totalTeethCount >= completionThreshold
    }


    private fun findCurrentSide(
        leftSideTeeth: Set<ToothNumber>,
        midSideTeeth: Set<ToothNumber>,
        rightSideTeeth: Set<ToothNumber>,
        result: Set<ToothNumber>
    ): JawSide {
        val leftIntersectCount = leftSideTeeth.intersect(result).size
        val midIntersectCount = midSideTeeth.intersect(result).size
        val rightIntersectCount = rightSideTeeth.intersect(result).size

        val intersectsCounts = listOf(leftIntersectCount, midIntersectCount, rightIntersectCount)
        val max = intersectsCounts.maxOrNull()
        val maxIndexes = intersectsCounts.indices.filter { intersectsCounts[it] == max }

        return if (maxIndexes.size > 1) {
            JawSide.MIDDLE
        } else {
            when (maxIndexes.firstOrNull()) {
                0 -> JawSide.LEFT
                1 -> JawSide.MIDDLE
                2 -> JawSide.RIGHT
                else -> JawSide.MIDDLE
            }
        }
    }




    fun detectCurrentSide(
        visibleSet: Set<String?>,
        missing: List<String>,
        jawType: JawType
    ): JawSide {
        val result = (visibleSet.filterNotNull().toSet() + missing).map {
            it.toToothNumber()
        }.toSet()

        return when (jawType) {
            JawType.UPPER -> {
                val leftSideTeeth = setOf(
                    ToothNumber.UL8,
                    ToothNumber.UL7,
                    ToothNumber.UL6,
                    ToothNumber.UL5,
                    ToothNumber.UL4
                )
                val midSideTeeth = setOf(
                    ToothNumber.UL3,
                    ToothNumber.UL2,
                    ToothNumber.UL1,
                    ToothNumber.UR1,
                    ToothNumber.UR2,
                    ToothNumber.UR3
                )
                val rightSideTeeth = setOf(
                    ToothNumber.UR8,
                    ToothNumber.UR7,
                    ToothNumber.UR6,
                    ToothNumber.UR5,
                    ToothNumber.UR4
                )
                findCurrentSide(leftSideTeeth, midSideTeeth, rightSideTeeth, result)
            }

            JawType.LOWER -> {
                val leftSideTeeth = setOf(
                    ToothNumber.LL8,
                    ToothNumber.LL7,
                    ToothNumber.LL6,
                    ToothNumber.LL5,
                    ToothNumber.LL4
                )
                val midSideTeeth = setOf(
                    ToothNumber.LL3,
                    ToothNumber.LL2,
                    ToothNumber.LL1,
                    ToothNumber.LR1,
                    ToothNumber.LR2,
                    ToothNumber.LR3
                )
                val rightSideTeeth = setOf(
                    ToothNumber.LR8,
                    ToothNumber.LR7,
                    ToothNumber.LR6,
                    ToothNumber.LR5,
                    ToothNumber.LR4
                )
                findCurrentSide(leftSideTeeth, midSideTeeth, rightSideTeeth, result)
            }

            else -> JawSide.MIDDLE
        }
    }

}

