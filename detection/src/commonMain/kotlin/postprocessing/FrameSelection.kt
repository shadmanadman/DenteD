package postprocessing

import model.AcceptedFrame
import model.JawSide


data class FrameScore(
    val frame: AcceptedFrame,
    val averageClarity: Double,
    val maxConfidence: Double,
    val coverage: Double
)


/**
 * Selects the best frames from a list of accepted frames based on clarity, confidence, and coverage.
 * It groups frames by jaw side, scores them, and then selects the top frames for each side.
 */
fun List<AcceptedFrame>.selectBestFrames(): List<AcceptedFrame> {
    val groupedFrames = this.groupBy { it.jawSide }

    val bestFrames = mutableListOf<AcceptedFrame>()

    groupedFrames.forEach { (section, framesInSection) ->
        val framesWithScore = framesInSection.map { frame ->
            val averageClarity = frame.boxes.map { it.clarityLevel }.average()
            val maxConfidence = frame.boxes.map { it.maxConf }.average()
            val coverage = frame.boxes.sumOf { it.width.toDouble() * it.height.toDouble() }
            FrameScore(frame, averageClarity, maxConfidence, coverage)
        }

        val sortedFrames = framesWithScore.filter { it.frame.frame!=null }.sortedWith(compareByDescending<FrameScore> {
            it.averageClarity
        }.thenByDescending {
            it.maxConfidence
        }.thenByDescending {
            it.coverage
        }).map { it.frame }

        val selectedFrames = when (section) {
            JawSide.LEFT, JawSide.RIGHT -> sortedFrames.take(2)
            JawSide.MIDDLE -> sortedFrames.take(1)
        }

        bestFrames.addAll(selectedFrames)
    }

    return bestFrames
}
