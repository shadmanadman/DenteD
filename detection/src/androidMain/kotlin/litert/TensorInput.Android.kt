package litert

import android.graphics.Color
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asAndroidBitmap
import androidx.core.graphics.scale
import androidx.core.graphics.get
import com.google.ai.edge.litert.TensorBuffer
import java.nio.ByteBuffer
import java.nio.ByteOrder

//@RequiresApi(Build.VERSION_CODES.O)
//actual fun ImageBitmap.toScaledByteBuffer(
//    inputWidth: Int,
//    inputHeight: Int,
//    inputAllocateSize: Int,
//    normalize: Boolean
//): PlatformTensorBuffer {
//    val bitmap = this.asAndroidBitmap().scale(inputWidth, inputHeight)
//    val byteBuffer = ByteBuffer.allocateDirect(inputAllocateSize)
//    byteBuffer.order(ByteOrder.nativeOrder())
//
//    for (y in 0 until inputHeight) {
//        for (x in 0 until inputWidth) {
//            val pixel = bitmap[x, y]
//
//            val r = Color.red(pixel)
//            val g = Color.green(pixel)
//            val b = Color.blue(pixel)
//
//            if (normalize) {
//                byteBuffer.putFloat(r / 255.0f)
//                byteBuffer.putFloat(g / 255.0f)
//                byteBuffer.putFloat(b / 255.0f)
//            } else {
//                byteBuffer.put(r.toByte())
//                byteBuffer.put(g.toByte())
//                byteBuffer.put(b.toByte())
//            }
//        }
//    }
//
//    return byteBuffer
//
//}

@RequiresApi(Build.VERSION_CODES.O)
actual fun ImageBitmap.toScaledByteBuffer(
    inputWidth: Int,
    inputHeight: Int,
    inputAllocateSize: Int,
    normalize: Boolean
): PlatformTensorBuffer {
    val bitmap = this.asAndroidBitmap().scale(inputWidth, inputHeight)

    // Each pixel = 3 channels (R,G,B)
    val floatArray = FloatArray(inputAllocateSize)

    var index = 0
    for (y in 0 until inputHeight) {
        for (x in 0 until inputWidth) {
            val pixel = bitmap[x, y]

            val r = Color.red(pixel).toFloat()
            val g = Color.green(pixel).toFloat()
            val b = Color.blue(pixel).toFloat()

            if (normalize) {
                floatArray[index++] = r / 255f
                floatArray[index++] = g / 255f
                floatArray[index++] = b / 255f
            } else {
                floatArray[index++] = r
                floatArray[index++] = g
                floatArray[index++] = b
            }
        }
    }

    return floatArray
}