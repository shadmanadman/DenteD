package litert

object Litert {
    private var interpreter: Interpreter? = null

    fun init(model: ByteArray, options: InterpreterOptions = InterpreterOptions()) {
        check(interpreter == null) { "Interpreter already initialized." }
        interpreter = Interpreter(model, options)
    }
}