package litert

import android.content.Context
import java.io.File

internal fun PlatformTensor.toTensor() = Tensor(this)

internal fun TensorDataType.toTensorDataType() = when(this){
    TensorDataType.FLOAT32 -> TensorDataType.FLOAT32
    TensorDataType.INT32 -> TensorDataType.INT32
    TensorDataType.UINT8 -> TensorDataType.UINT8
    TensorDataType.INT64 -> TensorDataType.INT64
}

internal fun ByteArray.writeToTempFile(context: Context, prefix: String = "model", suffix: String = ".tflite"): File {
    val tempFile = File.createTempFile(prefix, suffix, context.cacheDir)
    tempFile.outputStream().use { output ->
        output.write(this)
        output.flush()
        output.fd.sync()
    }
    return tempFile
}