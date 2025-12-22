package litert

object Litert {
    private var interpreter: Interpreter? = null

    fun init(model: ByteArray) {
        check(interpreter == null) { "Interpreter already initialized." }
        interpreter = Interpreter(model)
    }

    fun run(inputs: List<Any>, outputs: Map<Int, Any>) =
        interpreterOrThrow().run(inputs, outputs)

    private fun interpreterOrThrow(): Interpreter =
        interpreter ?: error("Interpreter not initialized. Call KfLite.init() first.")
}