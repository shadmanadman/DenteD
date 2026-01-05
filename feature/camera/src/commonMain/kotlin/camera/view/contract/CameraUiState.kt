package camera.view.contract

import postprocessing.NumberingResult
import shared.model.AcceptedFrame
import shared.model.CameraErrorState
import shared.model.DetectingStatus
import shared.model.FocusSection
import shared.model.ToothNumber

data class CameraUiState(
    val detectionStatus: DetectingStatus = DetectingStatus.Stopped,
    val currentFocusSection: FocusSection = FocusSection.MIDDLE,
    val acceptedTeeth: List<ToothNumber> = emptyList(),
    val acceptedFrames: List<AcceptedFrame> = emptyList(),
    val numberingResult: NumberingResult = NumberingResult(),
    val detectionErrorState: CameraErrorState = CameraErrorState.Ok
)