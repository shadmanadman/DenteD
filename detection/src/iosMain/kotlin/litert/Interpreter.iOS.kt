package litert

import cocoapods.TFLTensorFlowLite.TFLInterpreter
import cocoapods.TFLTensorFlowLite.TFLInterpreterOptions
import cocoapods.TFLTensorFlowLite.TFLMetalDelegate
import cocoapods.TFLTensorFlowLite.TFLMetalDelegateOptions
import cocoapods.TFLTensorFlowLite.TFLMetalDelegateThreadWaitType
import kotlinx.cinterop.ByteVar
import kotlinx.cinterop.ExperimentalForeignApi
import kotlinx.cinterop.readBytes
import kotlinx.cinterop.reinterpret
import platform.Foundation.NSData

actual class Interpreter actual constructor(val model: ByteArray) {

    @OptIn(ExperimentalForeignApi::class)
    private var tflInterpreter: TFLInterpreter? = null

    @OptIn(ExperimentalForeignApi::class)
    actual fun init(){
        val options = TFLInterpreterOptions().apply {
            setNumberOfThreads(4)
            setMetalDelegation()
            }

        tflInterpreter = errorHandled { errPtr ->
            TFLInterpreter(model.writeToTempFile(),options, errPtr)
        }!!

        errorHandled { errPtr ->
            val interpreter = requireNotNull(tflInterpreter) { "Interpreter has been closed or not initialized." }
            interpreter.allocateTensorsWithError(errPtr)
        }
    }
    @OptIn(ExperimentalForeignApi::class)
    actual fun run(inputs: List<Any>, outputs: Map<Int, Any>) {
        if (inputs.size > getInputTensorCount()) throw IllegalArgumentException("Wrong inputs dimension.")
        val interpreter = requireNotNull(tflInterpreter) { "Interpreter has been closed or not initialized." }


        // prepare input
        println("Input size: ${(inputs[0] as NSData).length}")
        inputs.forEachIndexed { index, input ->
            val inputTensor = getInputTensor(index)
            println("inputTensor: $input")
            errorHandled { errPtr ->
                inputTensor.platformTensor.copyData(
                    input as NSData,
                    errPtr
                )
            }
        }

        // Run inference
        errorHandled { errPtr ->
            interpreter.invokeWithError(errPtr)
        }

        // Collect outputs
        outputs.forEach { (index, outputContainer) ->
            val outputTensor = getOutputTensor(index)
            val outputData = errorHandled { errPtr ->
                outputTensor.platformTensor.dataWithError(errPtr)
            } ?: error("Failed to get output tensor data")

            val outputByteArray = ByteArray(outputData.length.toInt())
            outputData.bytes?.reinterpret<ByteVar>()?.readBytes(outputData.length.toInt())
                ?.copyInto(outputByteArray)
            val floatArray = outputByteArray.toFloatArray()
            println("Raw output bytes: ${floatArray.joinToString()}")

            val outputPtr = outputData.bytes?.reinterpret<ByteVar>()
            requireNotNull(outputPtr) { "outputData.bytes was null" }

            val typedOutput: Any = when (outputTensor.dataType) {
                TensorDataType.FLOAT32 -> outputPtr.readBytes(outputData.length.toInt())
                    .toFloatArray()

                TensorDataType.INT32 -> outputPtr.readBytes(outputData.length.toInt()).toIntArray()

                TensorDataType.UINT8 -> outputByteArray.map { it.toUByte() }.toUByteArray()

                TensorDataType.INT64 -> outputPtr.readBytes(outputData.length.toInt()).toLongArray()
            }

            // Assign the result to output container
            when (outputContainer) {
                is FloatArray -> (typedOutput as FloatArray).copyInto(outputContainer)
                is IntArray -> (typedOutput as IntArray).copyInto(outputContainer)
                is LongArray -> (typedOutput as LongArray).copyInto(outputContainer)
                is UIntArray -> (typedOutput as UIntArray).copyInto(outputContainer)
                // For Matrix types, we need to reshape the output
                is Array<*> -> {
                    if (outputContainer.isNotEmpty() && outputContainer[0] is Array<*>) {
                        @Suppress("UNCHECKED_CAST")
                        val outer = outputContainer as Array<Array<FloatArray>>

                        @Suppress("UNCHECKED_CAST")
                        val reshaped = typedOutput as FloatArray

                        val totalSize = outer.size * outer[0].size * outer[0][0].size
                        println("The outer size is ${outer.size} * ${outer[0].size} * ${outer[0][0].size}")
                        require(reshaped.size == totalSize) {
                            "Flat array size (${reshaped.size}) does not match output container size ($totalSize)"
                        }

                        var index = 0
                        for (i in outer.indices) {
                            for (j in outer[i].indices) {
                                for (k in outer[i][j].indices) {
                                    outer[i][j][k] = reshaped[index++]
                                }
                            }
                        }
                    } else {
                        error("Unsupported array shape in output container: ${outputContainer::class}")
                    }
                }

                else -> error("Unsupported output container type: ${outputContainer::class}")
            }
        }
    }


    /**
     * TFLInterpreter does not require explicit .close() in Objective-C/Swift,
     * releasing the reference is sufficient for ARC to clean it up
     */
    @OptIn(ExperimentalForeignApi::class)
    actual fun close() {
        tflInterpreter = null
    }

    /**
     * Gets the number of input tensors.
     */
    @OptIn(ExperimentalForeignApi::class)
    actual fun getInputTensorCount(): Int {
        val interpreter = requireNotNull(tflInterpreter) { "Interpreter has been closed or not initialized." }
        return interpreter.inputTensorCount().toInt()
    }

    /**
     * Gets the number of output Tensors.
     */
    @OptIn(ExperimentalForeignApi::class)
    actual fun getOutputTensorCount(): Int {
        val interpreter = requireNotNull(tflInterpreter) { "Interpreter has been closed or not initialized." }
        return interpreter.outputTensorCount().toInt()
    }

    /**
     * Gets the Tensor associated with the provdied input index.
     *
     * @throws IllegalArgumentException if [index] is negative or is not smaller than the
     * number of model inputs.
     */
    @OptIn(ExperimentalForeignApi::class)
    actual fun getInputTensor(index: Int): Tensor {
        val interpreter = requireNotNull(tflInterpreter) { "Interpreter has been closed or not initialized." }
        return errorHandled { errPtr ->
            interpreter.inputTensorAtIndex(index.toULong(), errPtr)
        }!!.toTensor()
    }

    /**
     * Gets the Tensor associated with the provdied output index.
     *
     * @throws IllegalArgumentException if [index] is negative or is not smaller than the
     * number of model inputs.
     */
    @OptIn(ExperimentalForeignApi::class)
    actual fun getOutputTensor(index: Int): Tensor {
        val interpreter = requireNotNull(tflInterpreter) { "Interpreter has been closed or not initialized." }
        return errorHandled { errPtr ->
            interpreter.outputTensorAtIndex(index.toULong(), errPtr)
        }!!.toTensor()
    }

    actual fun resizeInput(index: Int, shape: TensorShape) = Unit

}


@OptIn(ExperimentalForeignApi::class)
private fun setMetalDelegation() = TFLMetalDelegateOptions().apply {
    precisionLossAllowed = false
    quantizationEnabled = false
    waitType = TFLMetalDelegateThreadWaitType.TFLMetalDelegateThreadWaitTypeAggressive
    TFLMetalDelegate(options = this)
}