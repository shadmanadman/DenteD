package analyzer

import androidx.compose.ui.unit.IntRect
import kclarity.Kclarity
import shared.ext.cropWithIntRect
import shared.model.JawSide
import shared.model.ToothBox
import shared.platform.SharedImage

object ClarityLevel {
    fun SharedImage.determineClarityLevel(): Double{
        return Kclarity(this.toByteArray()?: byteArrayOf()).clarityLevel()
    }


    fun SharedImage.calibratedToothClarityLevel(
        visibleBoxes: List<ToothBox>
    ): Double {
        val middleTooth = when {
            visibleBoxes.size <= 2 -> visibleBoxes.first()
            visibleBoxes.size > 2 -> visibleBoxes[visibleBoxes.size / 2]
            else -> visibleBoxes.first()
        }

        val realX = ((middleTooth.x - (middleTooth.width / 2)) * 640)
        val realY = ((middleTooth.y - (middleTooth.height / 2)) * 640)
        val realWidth = (middleTooth.width * 640)
        val realHeight = (middleTooth.height * 640)

        val rect = IntRect(realX.toInt(), realY.toInt(), (realX + realWidth).toInt(), (realY + realHeight).toInt())
        val croppedBitmap = this.cropWithIntRect(rect)

        val clarityLevel = croppedBitmap.cropWithIntRect(rect).determineClarityLevel()
        println("Calibrated Tooth Clarity level:$clarityLevel")
        return clarityLevel
    }

    fun SharedImage.checkToothForAcceptedClarityLevel(
        calibratedClarityLevel: Double,
        jawSide: JawSide,
        visibleBoxes: MutableList<ToothBox>
    ): List<ToothBox> {
        val acceptedTooth = mutableListOf<String>()

        for (boxes in visibleBoxes) {
            val realX = ((boxes.x - (boxes.width / 2)) * 640)
            val realY = ((boxes.y - (boxes.height / 2)) * 640)
            val realWidth = (boxes.width * 640)
            val realHeight = (boxes.height * 640)
            try {
                val rect = IntRect(realX.toInt(), realY.toInt(), (realX + realWidth).toInt(), (realY + realHeight).toInt())
                val croppedBitmap = this.cropWithIntRect(rect)

                val clarityLevel = croppedBitmap.determineClarityLevel()
                println("Clarity level for this image:$clarityLevel")
                boxes.clarityLevel = clarityLevel
                if (clarityLevel >= calibratedClarityLevel * 0.65) {
                    if (jawSide == JawSide.MIDDLE) {
                        acceptedTooth.addAll(visibleBoxes.map { it.alphabeticNumber })
                        break
                    } else {
                        acceptedTooth.add(boxes.alphabeticNumber)
                    }
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }


        return visibleBoxes
    }
}