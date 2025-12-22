package analyzer

import android.content.Context
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import startup.AppContext
import kotlin.math.PI

class MotionManager : SensorEventListener {

    private val context by lazy { AppContext.get() }
    private val sensorManager = context.getSystemService(Context.SENSOR_SERVICE) as SensorManager
    private var gravitySensor: Sensor? = null

    var angle: Double = 0.0

    private val sessionQueue = android.os.Handler(android.os.Looper.getMainLooper())

    fun registerMotionListener() {
        gravitySensor = sensorManager.getDefaultSensor(Sensor.TYPE_ROTATION_VECTOR)
        gravitySensor?.also { sensor ->
            sensorManager.registerListener(this, sensor, SensorManager.SENSOR_DELAY_UI)
        }
    }

    override fun onSensorChanged(event: SensorEvent) {
        if (event.sensor.type == Sensor.TYPE_ROTATION_VECTOR) {
            val rotationMatrix = FloatArray(9)
            SensorManager.getRotationMatrixFromVector(rotationMatrix, event.values)
            val orientationAngles = FloatArray(3)
            SensorManager.getOrientation(rotationMatrix, orientationAngles)

            // Get the pitch (in radians) and convert to degrees.
            val pitch = orientationAngles[1]
            val angleInDegrees = pitch * 180.0 / PI

            sessionQueue.post {
               angle = angleInDegrees
            }
        }
    }

    override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {
        // Handle accuracy changes if needed
    }

    fun stopMotionListener() {
        sensorManager.unregisterListener(this)
    }
}