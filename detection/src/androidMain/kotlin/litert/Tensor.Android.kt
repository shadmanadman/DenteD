package litert

actual class Tensor(
    internal val platformTensor: PlatformTensor?
) {
    actual val dataType: TensorDataType
        get() = platformTensor?.dataType?:TensorDataType.FLOAT32
    actual val name: String
        get() = platformTensor?.name ?:""
    actual val shape: IntArray
        get() = platformTensor?.shape?:intArrayOf()
}
