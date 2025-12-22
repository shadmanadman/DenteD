package analyzer

actual object MotionListener {
    private val motionManager = MotionManager()

    actual var deviceAngel: Double = 0.0

    actual fun registerMotionListener() {
        motionManager.registerMotionListener()
        deviceAngel = motionManager.angle
    }

    actual fun unregisterMotionListener() {
        motionManager.stopMotionListener()
    }
}