package litert

expect class Tensor {
    val dataType: TensorDataType
    val name: String
    val shape: IntArray
}