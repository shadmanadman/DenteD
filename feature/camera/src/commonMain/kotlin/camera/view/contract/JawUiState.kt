package camera.view.contract

import shared.model.FrameAnalyzeStatus
import shared.model.JawSide
import shared.model.JawSideStatus
import shared.model.JawType
import shared.model.ToothDetectionStatus
import shared.model.ToothNumber

data class JawUiState(
    val selectedJaw: List<JawType> = listOf(JawType.UPPER, JawType.LOWER, JawType.FRONT),
    val jawType: JawType = selectedJaw.first(),
    val jawSide: JawSide = JawSide.LEFT,
    var upperIllustrationTeeth: Map<ToothNumber, ToothDetectionStatus> = mapOf(
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
    ),
    var lowerIllustrationTeeth: Map<ToothNumber, ToothDetectionStatus> = mapOf(
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
    ),
    var frontIllustrationTeeth: Map<ToothNumber, ToothDetectionStatus> = mapOf(
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
    ),
    val jawProgress: Map<JawType, Int> = mapOf(
        JawType.UPPER to 0,
        JawType.LOWER to 0,
        JawType.FRONT to 0
    ),
    val jawSideAnalyzeState: Map<JawSideStatus, FrameAnalyzeStatus> = mapOf(
        JawSideStatus.LOWER_LEFT to FrameAnalyzeStatus.None,
        JawSideStatus.LOWER_RIGHT to FrameAnalyzeStatus.None,
        JawSideStatus.LOWER_MIDDLE to FrameAnalyzeStatus.None,
        JawSideStatus.UPPER_LEFT to FrameAnalyzeStatus.None,
        JawSideStatus.UPPER_RIGHT to FrameAnalyzeStatus.None,
        JawSideStatus.UPPER_MIDDLE to FrameAnalyzeStatus.None,
        JawSideStatus.FRONT to FrameAnalyzeStatus.None,
    ),
    val allJawsCompleted: Boolean = false,
    val averageJawsProgress: Int = 0
)
