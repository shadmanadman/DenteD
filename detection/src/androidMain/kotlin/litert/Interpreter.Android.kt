package litert

import android.content.Context
import com.google.ai.edge.litert.Accelerator
import com.google.ai.edge.litert.CompiledModel
import com.google.ai.edge.litert.Model
import com.google.ai.edge.litert.TensorBuffer
import startup.AppContext
import java.nio.ByteBuffer

actual class Interpreter actual constructor(model: ByteArray) {
    private val context: Context by lazy { AppContext.get() }
    private val model = Model.load(model.writeToTempFile(context).path)
    private lateinit var compiledModel : CompiledModel
    private lateinit var inputBuffers : List<TensorBuffer>
    private lateinit var outputBuffers : List<TensorBuffer>

    private val tensorFlowInterpreter = PlatformInterpreter(model)

    actual fun init(){
        compiledModel = CompiledModel.create(model = model, options = CompiledModel.Options(Accelerator.GPU))
        inputBuffers = compiledModel.createInputBuffers()
        outputBuffers = compiledModel.createOutputBuffers()
    }
    actual fun run(inputs: ModelInput, outputs: ModelOutput) {
        // prepare input
        val firstInput = inputs[0] as FloatArray
        inputBuffers[0].writeFloat(firstInput)

        compiledModel.run(inputBuffers, outputBuffers)

        // Collect output
        outputs.forEach { (index, target) ->
            val tensor = outputBuffers[index]

            when (target) {

                is FloatArray -> {
                    val floats = tensor.readFloat()
                    System.arraycopy(floats, 0, target, 0, floats.size)
                }

                is IntArray -> {
                    val ints = tensor.readInt()
                    System.arraycopy(ints, 0, target, 0, ints.size)
                }

                is ByteArray -> {
                    val bytes = tensor.readInt8()
                    System.arraycopy(bytes, 0, target, 0, bytes.size)
                }

                is ByteBuffer -> {
                    target.clear()
                    val arr = tensor.readInt8()
                    target.put(arr)
                    target.flip()
                }

                else -> {
                    error("Unsupported output type for index=$index: ${target::class}")
                }
            }
        }
    }

    actual fun close() {
        compiledModel.close()
    }

    actual fun getInputTensorCount() = inputBuffers.size


    actual fun getOutputTensorCount() = inputBuffers.size

    actual fun getInputTensor(index: Int) : Tensor = tensorFlowInterpreter.getInputTensor(index).toTensor()

    actual fun getOutputTensor(index: Int): Tensor = tensorFlowInterpreter.getOutputTensor(index).toTensor()

    actual fun resizeInput(index: Int, shape: TensorShape) = Unit

}