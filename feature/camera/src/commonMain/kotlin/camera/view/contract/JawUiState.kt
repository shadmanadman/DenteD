package camera.view.contract

import shared.model.FrameAnalyzeStatus
import shared.model.JawSide
import shared.model.JawSideStatus
import shared.model.JawType
import shared.model.ToothDetectionStatus
import shared.model.ToothNumber

data class JawUiState(
    val jawType: JawType = JawType.UPPER,
    val jawSide: JawSide = JawSide.LEFT,
    var upperIllustrationTeeth: Map<ToothNumber, ToothDetectionStatus> = mapOf(),
    var lowerIllustrationTeeth: Map<ToothNumber, ToothDetectionStatus> = mapOf(),
    var frontIllustrationTeeth: Map<ToothNumber, ToothDetectionStatus> = mapOf(),
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
