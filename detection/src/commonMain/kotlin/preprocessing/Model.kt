package preprocessing

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

