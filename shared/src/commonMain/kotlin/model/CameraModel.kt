package model

enum class FocusSection {
    NEAR,
    MIDDLE,
    FARTHEST}

enum class CameraErrorState {
    MoveAway,
    MoveMuchAway,
    MoveCloser,
    MoveMuchCloser,
    BadAngel,
    Ok
}

data class FocusPoints(val meteringPoint: MeteringPointPlatform, val focusSection: FocusSection)
data class MeteringPointPlatform(val x: Float = 0f, val y: Float = 0f,val size: Float = 3f)
