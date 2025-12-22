package analyzer

import model.JawSide
import model.JawType
import model.ToothBox


expect object MotionListener {
    var deviceAngel : Double
    fun registerMotionListener()
    fun unregisterMotionListener()
}


/**
 * checks if current visible teeth are in the correct angle for the current jaw type
 */
fun deviceAngelChecker(
    visibleBoxes: List<ToothBox>,
    jawType: JawType,
    jawSide: JawSide
): Boolean {
    val numbersSet = visibleBoxes.map { it.alphabeticNumber }

    var averageOfYDistances = 0.0

    if (jawType == JawType.FRONT || jawSide == JawSide.MIDDLE)
        return false

    if (jawType == JawType.LOWER) {
        var count = 0.0

        if (numbersSet.contains("ll8") ||
            numbersSet.contains("ll7") ||
            numbersSet.contains("ll6") ||
            numbersSet.contains("ll5")
        ) {
            var lastYDistance = 0F
            if (numbersSet.contains("ll8") && numbersSet.contains("ll6")) {
                count += 1
                val ll8 = visibleBoxes.first { it.alphabeticNumber == "ll8" }
                val ll6 = visibleBoxes.first { it.alphabeticNumber == "ll6" }
                lastYDistance = ll6.y - ll8.y
            }

            var midYDistance = 0F
            if (numbersSet.contains("ll7") && numbersSet.contains("ll5")) {
                count += 1
                val ll7 = visibleBoxes.first { it.alphabeticNumber == "ll7" }
                val ll5 = visibleBoxes.first { it.alphabeticNumber == "ll5" }
                midYDistance = ll5.y - ll7.y
            }

            var firstYDistance = 0f
            if (numbersSet.contains("ll6") && numbersSet.contains("ll4")) {
                count += 1
                val ll6 = visibleBoxes.first { it.alphabeticNumber == "ll6" }
                val ll4 = visibleBoxes.first { it.alphabeticNumber == "ll4" }
                firstYDistance = ll4.y - ll6.y
            }

            averageOfYDistances = (lastYDistance + midYDistance + firstYDistance) / count
        } else if (numbersSet.contains("lr8") ||
            numbersSet.contains("lr7") ||
            numbersSet.contains("lr6") ||
            numbersSet.contains("lr5")
        ) {
            var lastYDistance = 0f
            if (numbersSet.contains("lr8") && numbersSet.contains("lr6")) {
                count += 1
                val lr8 = visibleBoxes.first { it.alphabeticNumber == "lr8" }
                val lr6 = visibleBoxes.first { it.alphabeticNumber == "lr6" }
                lastYDistance = lr6.y - lr8.y
            }

            var midYDistance = 0f
            if (numbersSet.contains("lr7") && numbersSet.contains("lr5")) {
                count += 1
                val lr7 = visibleBoxes.first { it.alphabeticNumber == "lr7" }
                val lr5 = visibleBoxes.first { it.alphabeticNumber == "lr5" }
                midYDistance = lr5.y - lr7.y
            }

            var firstYDistance = 0f
            if (numbersSet.contains("lr6") && numbersSet.contains("lr4")) {
                count += 1
                val lr6 = visibleBoxes.first { it.alphabeticNumber == "lr6" }
                val lr4 = visibleBoxes.first { it.alphabeticNumber == "lr4" }
                firstYDistance = lr4.y - lr6.y
            }

            averageOfYDistances = (lastYDistance + midYDistance + firstYDistance) / count
        }

        if (averageOfYDistances != 0.0 && averageOfYDistances < 0.25) {
            return true
        }
    } else {
        if (MotionListener.deviceAngel > 65) {
            return true
        }

        var count = 0.0

        if (numbersSet.contains("ul8") ||
            numbersSet.contains("ul7") ||
            numbersSet.contains("ul6") ||
            numbersSet.contains("ul5")
        ) {
            var lastYDistance = 0f
            if (numbersSet.contains("ul8") && numbersSet.contains("ul6")) {
                count += 1
                val ul8 = visibleBoxes.first { it.alphabeticNumber == "ul8" }
                val ul6 = visibleBoxes.first { it.alphabeticNumber == "ul6" }
                lastYDistance = ul8.y - ul6.y
            }

            var midYDistance = 0f
            if (numbersSet.contains("ul7") && numbersSet.contains("ul5")) {
                count += 1
                val ul7 = visibleBoxes.first { it.alphabeticNumber == "ul7" }
                val ul5 = visibleBoxes.first { it.alphabeticNumber == "ul5" }
                midYDistance = ul7.y - ul5.y
            }

            var firstYDistance = 0f
            if (numbersSet.contains("ul6") && numbersSet.contains("ul4")) {
                count += 1
                val ul6 = visibleBoxes.first { it.alphabeticNumber == "ul6" }
                val ul4 = visibleBoxes.first { it.alphabeticNumber == "ul4" }
                firstYDistance = ul4.y - ul6.y
            }

            averageOfYDistances = (lastYDistance + midYDistance + firstYDistance) / count
        } else if (numbersSet.contains("ur8") ||
            numbersSet.contains("ur7") ||
            numbersSet.contains("ur6") ||
            numbersSet.contains("ur5")
        ) {
            var lastYDistance = 0f
            if (numbersSet.contains("ur8") && numbersSet.contains("ur6")) {
                count += 1
                val ur8 = visibleBoxes.first { it.alphabeticNumber == "ur8" }
                val ur6 = visibleBoxes.first { it.alphabeticNumber == "ur6" }
                lastYDistance = ur8.y - ur6.y
            }

            var midYDistance = 0f
            if (numbersSet.contains("ur7") && numbersSet.contains("ur5")) {
                count += 1
                val ur7 = visibleBoxes.first { it.alphabeticNumber == "ur7" }
                val ur5 = visibleBoxes.first { it.alphabeticNumber == "ur5" }
                midYDistance = ur5.y - ur7.y
            }

            var firstYDistance = 0f
            if (numbersSet.contains("ur6") && numbersSet.contains("ur4")) {
                count += 1
                val ur6 = visibleBoxes.first { it.alphabeticNumber == "ur6" }
                val ur4 = visibleBoxes.first { it.alphabeticNumber == "ur4" }
                firstYDistance = ur4.y - ur6.y
            }

            averageOfYDistances = (lastYDistance + midYDistance + firstYDistance) / count
        }

        if (averageOfYDistances != 0.0 && averageOfYDistances < 0.21) {
            return true
        }
    }

    return false
}