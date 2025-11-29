package litert

internal expect class Interpreter(model: ByteArray) {
    fun init()
    /**
     * Runs model inference if the model takes multiple inputs, or returns multiple outputs.
     */
    fun run(inputs: List<Any>, outputs: Map<Int, Any>)

    /**
     * Release resources associated with the [Interpreter].
     */
    fun close()

    /**
     * Gets the number of input tensors.
     */
    fun getInputTensorCount(): Int

    /**
     * Gets the number of output Tensors.
     */
    fun getOutputTensorCount(): Int

    /**
     * Gets the Tensor associated with the provided input index.
     *
     * @throws IllegalArgumentException if [index] is negative or is not smaller than the
     * number of model inputs.
     */
    fun getInputTensor(index: Int): Tensor

    /**
     * Gets the Tensor associated with the provided output index.
     *
     * @throws IllegalArgumentException if [index] is negative or is not smaller than the
     * number of model inputs.
     */
    fun getOutputTensor(index: Int): Tensor

    /**
     * Resizes [index] input of the native model to the given [shape].
     */
    fun resizeInput(index: Int, shape: TensorShape)
}