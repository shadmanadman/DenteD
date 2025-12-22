package analyzer

import platform.CoreMotion.CMDeviceMotion
import platform.CoreMotion.CMMotionManager
import platform.Foundation.NSError
import platform.Foundation.NSOperationQueue
import kotlin.math.PI

class MotionManager {
    private val motionManager = CMMotionManager()
    private val queue = NSOperationQueue()

    var angle: Double = 0.0

    fun startMotionListener() {
        if (!motionManager.isDeviceMotionAvailable()) {
            IllegalStateException("Device Motion is not available on this device.")
            return
        }
        motionManager.setDeviceMotionUpdateInterval(1.0 / 60.0)
        motionManager.startDeviceMotionUpdatesToQueue(queue) { motion: CMDeviceMotion?, error: NSError? ->

            if (error != null) {
                // If there's an error, close the flow
                RuntimeException("Core Motion error: ${error.localizedDescription}")
                return@startDeviceMotionUpdatesToQueue
            }

            motion?.attitude?.let { attitude ->
                val pitchDegrees = attitude.pitch * 180.0 / PI
                angle = pitchDegrees
            }
        }
    }

    fun stopMotionListener() {
        motionManager.stopDeviceMotionUpdates()
    }
}