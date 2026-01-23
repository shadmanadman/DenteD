package shared.ext

import shared.model.JawSide
import shared.model.JawSideStatus
import shared.model.JawType
import shared.model.ToothNumber

val lowerLeftSide = listOf(
    ToothNumber.LL8,
    ToothNumber.LL7,
    ToothNumber.LL6,
    ToothNumber.LL5,
    ToothNumber.LL4
)
val lowerRightSide = listOf(
    ToothNumber.LR8,
    ToothNumber.LR7,
    ToothNumber.LR6,
    ToothNumber.LR5,
    ToothNumber.LR4
)

val lowerMiddleSide = listOf(
    ToothNumber.LL3,
    ToothNumber.LL2,
    ToothNumber.LL1,
    ToothNumber.LR1,
    ToothNumber.LR2,
    ToothNumber.LR3
)

val upperMiddleSide = listOf(
    ToothNumber.UL3,
    ToothNumber.UL2,
    ToothNumber.UL1,
    ToothNumber.UR1,
    ToothNumber.UR2,
    ToothNumber.UR3
)

val upperRightSide = listOf(
    ToothNumber.UR8,
    ToothNumber.UR7,
    ToothNumber.UR6,
    ToothNumber.UR5,
    ToothNumber.UR4
)

val upperLeftSide = listOf(
    ToothNumber.UL8,
    ToothNumber.UL7,
    ToothNumber.UL6,
    ToothNumber.UL5,
    ToothNumber.UL4
)

val frontSide = listOf(
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

fun convertToJawStatus(jawSide: JawSide, jawType: JawType): JawSideStatus {
    return when (jawType) {
        JawType.LOWER -> {
            when (jawSide) {
                JawSide.LEFT -> JawSideStatus.LOWER_LEFT
                JawSide.RIGHT -> JawSideStatus.LOWER_RIGHT
                else -> JawSideStatus.LOWER_MIDDLE
            }
        }

        JawType.UPPER -> {
            when (jawSide) {
                JawSide.LEFT -> JawSideStatus.UPPER_LEFT
                JawSide.RIGHT -> JawSideStatus.UPPER_RIGHT
                else -> JawSideStatus.UPPER_MIDDLE
            }
        }

        else -> JawSideStatus.FRONT
    }
}


fun List<ToothNumber>.toJawType(): List<JawType> {
    val selectedJaw = mutableListOf<JawType>()
    if (this.any { toothNumber -> frontSide.contains(toothNumber) })
        selectedJaw.add(JawType.FRONT)
    if (this.any { toothNumber ->
            upperLeftSide.contains(toothNumber) || upperRightSide.contains(
                toothNumber
            ) || upperMiddleSide.contains(toothNumber)
        })
        selectedJaw.add(JawType.UPPER)
    if (this.any { toothNumber ->
            lowerLeftSide.contains(toothNumber) || lowerRightSide.contains(
                toothNumber
            ) || lowerMiddleSide.contains(toothNumber)
        })
        selectedJaw.add(JawType.LOWER)
    return selectedJaw
}

fun ToothNumber.toIconIndex(): Int {
    return when (this) {
        ToothNumber.UL1 -> 7
        ToothNumber.UL2 -> 6
        ToothNumber.UL3 -> 5
        ToothNumber.UL4 -> 4
        ToothNumber.UL5 -> 3
        ToothNumber.UL6 -> 2
        ToothNumber.UL7 -> 1
        ToothNumber.UL8 -> 0
        ToothNumber.UR1 -> 8
        ToothNumber.UR2 -> 9
        ToothNumber.UR3 -> 10
        ToothNumber.UR4 -> 11
        ToothNumber.UR5 -> 12
        ToothNumber.UR6 -> 13
        ToothNumber.UR7 -> 14
        ToothNumber.UR8 -> 15
        ToothNumber.LL1 -> 7
        ToothNumber.LL2 -> 6
        ToothNumber.LL3 -> 5
        ToothNumber.LL4 -> 4
        ToothNumber.LL5 -> 3
        ToothNumber.LL6 -> 2
        ToothNumber.LL7 -> 1
        ToothNumber.LL8 -> 0
        ToothNumber.LR1 -> 8
        ToothNumber.LR2 -> 9
        ToothNumber.LR3 -> 10
        ToothNumber.LR4 -> 11
        ToothNumber.LR5 -> 12
        ToothNumber.LR6 -> 13
        ToothNumber.LR7 -> 14
        ToothNumber.LR8 -> 15
    }
}