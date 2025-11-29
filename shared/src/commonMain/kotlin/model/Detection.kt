package model

import androidx.compose.ui.graphics.ImageBitmap

enum class JawPosition { UpperJaw, LowerJaw, FrontTeeth, FrontTeethUpper, FrontTeethLower }
enum class JawSide {LEFT,RIGHT,MIDDLE}
data class DetectionRow(
    val toothCategory: ToothBox,
    val toothSegmentationPoint: List<ToothSegmentationPoint>
)

data class ToothSegmentationPoint(val x: Float, val y: Float)
data class ToothBox(
    var category: Int,
    var x: Float,
    val y: Float,
    val width: Float,
    val height: Float,
    val maxConf: Float,
    var number: Int,
    var alphabeticNumber: String,
    var clarityLevel:Double = 0.0)

enum class DetectingStatus {
    Stopped,
    Started
}

enum class FrameAnalyzeStatus {
    None,
    Started,
    Completed
}

enum class JawSideStatus {
    UPPER_LEFT,
    UPPER_RIGHT,
    UPPER_MIDDLE,
    LOWER_LEFT,
    LOWER_RIGHT,
    LOWER_MIDDLE,
    FRONT
}

enum class JawType{
    UPPER,
    LOWER,
    FRONT
}

data class AcceptedFrame(
    val frameWithToothBox: ImageBitmap? = null,
    val frame: ImageBitmap? = null,
    val jawSide: JawSide = JawSide.LEFT,
    val jawType: JawType = JawType.UPPER,
    val boxes: List<ToothBox> = listOf(),
    var fileName: String = ""
)
enum class ToothDetectionStatus {
    INITIAL,
    DETECTED
}


enum class ToothNumber {
    UL1,
    UL2,
    UL3,
    UL4,
    UL5,
    UL6,
    UL7,
    UL8,
    UR1,
    UR2,
    UR3,
    UR4,
    UR5,
    UR6,
    UR7,
    UR8,
    LL1,
    LL2,
    LL3,
    LL4,
    LL5,
    LL6,
    LL7,
    LL8,
    LR1,
    LR2,
    LR3,
    LR4,
    LR5,
    LR6,
    LR7,
    LR8
}

