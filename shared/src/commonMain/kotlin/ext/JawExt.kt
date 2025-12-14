package ext

import model.JawSide
import model.JawSideStatus
import model.JawType
import model.ToothNumber

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