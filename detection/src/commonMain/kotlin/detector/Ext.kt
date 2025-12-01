package detector

import androidx.compose.ui.graphics.Canvas
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.Paint
import ext.resize
import model.JawSide
import model.JawType
import model.ToothNumber
import kotlin.math.max

fun ImageBitmap.squareMe(maxBound: Int = 640): ImageBitmap {
    val imageWidth = this.width.toFloat()
    val imageHeight = this.height.toFloat()
    val maxSize = maxBound.toFloat()

    // Scale factor for fitting the image inside maxBound
    val scale = max(imageWidth, imageHeight) / maxSize
    val resizedWidth = imageWidth / scale
    val resizedHeight = imageHeight / scale

    // Step 1: Resize the ImageBitmap
    val resized = this.resize(
        resizedWidth.toInt(),
        resizedHeight.toInt()
    )

    // Step 2: Create a square ImageBitmap (maxBound × maxBound)
    val output = ImageBitmap(width = maxBound, height = maxBound)

    val canvas = Canvas(output)
    val paint = Paint()

    // Fill background (you can change this color)
    paint.color = Color.DarkGray
    canvas.drawRect(
        left = 0f,
        top = 0f,
        right = maxSize,
        bottom = maxSize,
        paint = paint
    )

    // Step 3: Center the resized image
    val dx = (maxSize - resizedWidth) / 2f
    val dy = (maxSize - resizedHeight) / 2f

    canvas.drawImage(
        image = resized,
        topLeftOffset = androidx.compose.ui.geometry.Offset(dx, dy),
        paint = paint
    )

    return output
}

fun ImageBitmap.calculateNormalizedPadding(targetSize: Float = 640f): Float {
    val scale = this.height / targetSize
    val resizedWidth = this.width / scale
    val dx = (targetSize - resizedWidth) / 2f

    return dx / targetSize   // normalized padding
}

fun String.toToothNumber(): ToothNumber {
    return when (this) {
        "ul1" -> ToothNumber.UL1
        "ul2" -> ToothNumber.UL2
        "ul3" -> ToothNumber.UL3
        "ul4" -> ToothNumber.UL4
        "ul5" -> ToothNumber.UL5
        "ul6" -> ToothNumber.UL6
        "ul7" -> ToothNumber.UL7
        "ul8" -> ToothNumber.UL8
        "ur1" -> ToothNumber.UR1
        "ur2" -> ToothNumber.UR2
        "ur3" -> ToothNumber.UR3
        "ur4" -> ToothNumber.UR4
        "ur5" -> ToothNumber.UR5
        "ur6" -> ToothNumber.UR6
        "ur7" -> ToothNumber.UR7
        "ur8" -> ToothNumber.UR8
        "ll1" -> ToothNumber.LL1
        "ll2" -> ToothNumber.LL2
        "ll3" -> ToothNumber.LL3
        "ll4" -> ToothNumber.LL4
        "ll5" -> ToothNumber.LL5
        "ll6" -> ToothNumber.LL6
        "ll7" -> ToothNumber.LL7
        "ll8" -> ToothNumber.LL8
        "lr1" -> ToothNumber.LR1
        "lr2" -> ToothNumber.LR2
        "lr3" -> ToothNumber.LR3
        "lr4" -> ToothNumber.LR4
        "lr5" -> ToothNumber.LR5
        "lr6" -> ToothNumber.LR6
        "lr7" -> ToothNumber.LR7
        "lr8" -> ToothNumber.LR8
        else -> throw IllegalArgumentException("Invalid tooth number: $this")
    }
}

fun ToothNumber.toStringRepresentation(): String {
    return when (this) {
        ToothNumber.UL1 -> "ul1"
        ToothNumber.UL2 -> "ul2"
        ToothNumber.UL3 -> "ul3"
        ToothNumber.UL4 -> "ul4"
        ToothNumber.UL5 -> "ul5"
        ToothNumber.UL6 -> "ul6"
        ToothNumber.UL7 -> "ul7"
        ToothNumber.UL8 -> "ul8"
        ToothNumber.UR1 -> "ur1"
        ToothNumber.UR2 -> "ur2"
        ToothNumber.UR3 -> "ur3"
        ToothNumber.UR4 -> "ur4"
        ToothNumber.UR5 -> "ur5"
        ToothNumber.UR6 -> "ur6"
        ToothNumber.UR7 -> "ur7"
        ToothNumber.UR8 -> "ur8"
        ToothNumber.LL1 -> "ll1"
        ToothNumber.LL2 -> "ll2"
        ToothNumber.LL3 -> "ll3"
        ToothNumber.LL4 -> "ll4"
        ToothNumber.LL5 -> "ll5"
        ToothNumber.LL6 -> "ll6"
        ToothNumber.LL7 -> "ll7"
        ToothNumber.LL8 -> "ll8"
        ToothNumber.LR1 -> "lr1"
        ToothNumber.LR2 -> "lr2"
        ToothNumber.LR3 -> "lr3"
        ToothNumber.LR4 -> "lr4"
        ToothNumber.LR5 -> "lr5"
        ToothNumber.LR6 -> "lr6"
        ToothNumber.LR7 -> "lr7"
        ToothNumber.LR8 -> "lr8"
    }
}



fun ToothNumber.getTheJawType():JawType{
    return when(this){
        ToothNumber.UL1 -> JawType.UPPER
        ToothNumber.UL2 -> JawType.UPPER
        ToothNumber.UL3 -> JawType.UPPER
        ToothNumber.UL4 -> JawType.UPPER
        ToothNumber.UL5 -> JawType.UPPER
        ToothNumber.UL6 -> JawType.UPPER
        ToothNumber.UL7 -> JawType.UPPER
        ToothNumber.UL8 -> JawType.UPPER
        ToothNumber.UR1 -> JawType.UPPER
        ToothNumber.UR2 -> JawType.UPPER
        ToothNumber.UR3 -> JawType.UPPER
        ToothNumber.UR4 -> JawType.UPPER
        ToothNumber.UR5 -> JawType.UPPER
        ToothNumber.UR6 -> JawType.UPPER
        ToothNumber.UR7 -> JawType.UPPER
        ToothNumber.UR8 -> JawType.UPPER
        ToothNumber.LL1 -> JawType.LOWER
        ToothNumber.LL2 -> JawType.LOWER
        ToothNumber.LL3 -> JawType.LOWER
        ToothNumber.LL4 -> JawType.LOWER
        ToothNumber.LL5 -> JawType.LOWER
        ToothNumber.LL6 -> JawType.LOWER
        ToothNumber.LL7 -> JawType.LOWER
        ToothNumber.LL8 -> JawType.LOWER
        ToothNumber.LR1 -> JawType.LOWER
        ToothNumber.LR2 -> JawType.LOWER
        ToothNumber.LR3 -> JawType.LOWER
        ToothNumber.LR4 -> JawType.LOWER
        ToothNumber.LR5 -> JawType.LOWER
        ToothNumber.LR6 -> JawType.LOWER
        ToothNumber.LR7 -> JawType.LOWER
        ToothNumber.LR8 -> JawType.LOWER
    }
}

fun ToothNumber.getTheJawSide(): JawSide {
    return when(this){
        ToothNumber.UL1 -> JawSide.LEFT
        ToothNumber.UL2 -> JawSide.LEFT
        ToothNumber.UL3 -> JawSide.LEFT
        ToothNumber.UL4 -> JawSide.LEFT
        ToothNumber.UL5 -> JawSide.LEFT
        ToothNumber.UL6 -> JawSide.LEFT
        ToothNumber.UL7 -> JawSide.LEFT
        ToothNumber.UL8 -> JawSide.LEFT
        ToothNumber.UR1 -> JawSide.RIGHT
        ToothNumber.UR2 -> JawSide.RIGHT
        ToothNumber.UR3 -> JawSide.RIGHT
        ToothNumber.UR4 -> JawSide.RIGHT
        ToothNumber.UR5 -> JawSide.RIGHT
        ToothNumber.UR6 -> JawSide.RIGHT
        ToothNumber.UR7 -> JawSide.RIGHT
        ToothNumber.UR8 -> JawSide.RIGHT
        ToothNumber.LL1 -> JawSide.LEFT
        ToothNumber.LL2 -> JawSide.LEFT
        ToothNumber.LL3 -> JawSide.LEFT
        ToothNumber.LL4 -> JawSide.LEFT
        ToothNumber.LL5 -> JawSide.LEFT
        ToothNumber.LL6 -> JawSide.LEFT
        ToothNumber.LL7 -> JawSide.LEFT
        ToothNumber.LL8 -> JawSide.LEFT
        ToothNumber.LR1 -> JawSide.RIGHT
        ToothNumber.LR2 -> JawSide.RIGHT
        ToothNumber.LR3 -> JawSide.RIGHT
        ToothNumber.LR4 -> JawSide.RIGHT
        ToothNumber.LR5 -> JawSide.RIGHT
        ToothNumber.LR6 -> JawSide.RIGHT
        ToothNumber.LR7 -> JawSide.RIGHT
        ToothNumber.LR8 -> JawSide.RIGHT
    }
}

fun ToothNumber.toActualNumber():Int{
    return when(this){
        ToothNumber.UL1 -> 1
        ToothNumber.UL2 -> 2
        ToothNumber.UL3 -> 3
        ToothNumber.UL4 -> 4
        ToothNumber.UL5 -> 5
        ToothNumber.UL6 -> 6
        ToothNumber.UL7 -> 7
        ToothNumber.UL8 -> 8
        ToothNumber.UR1 -> 1
        ToothNumber.UR2 -> 2
        ToothNumber.UR3 -> 3
        ToothNumber.UR4 -> 4
        ToothNumber.UR5 -> 5
        ToothNumber.UR6 -> 6
        ToothNumber.UR7 -> 7
        ToothNumber.UR8 -> 8
        ToothNumber.LL1 -> 1
        ToothNumber.LL2 -> 2
        ToothNumber.LL3 -> 3
        ToothNumber.LL4 -> 4
        ToothNumber.LL5 -> 5
        ToothNumber.LL6 -> 6
        ToothNumber.LL7 -> 7
        ToothNumber.LL8 -> 8
        ToothNumber.LR1 -> 1
        ToothNumber.LR2 -> 2
        ToothNumber.LR3 -> 3
        ToothNumber.LR4 -> 4
        ToothNumber.LR5 -> 5
        ToothNumber.LR6 -> 6
        ToothNumber.LR7 -> 7
        ToothNumber.LR8 -> 8
    }
}
