package camera.controller

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Bitmap.createBitmap
import android.hardware.camera2.CameraAccessException
import android.hardware.camera2.CameraCharacteristics
import android.hardware.camera2.CameraManager
import android.hardware.camera2.CameraMetadata.LENS_FACING_BACK
import androidx.camera.core.Camera
import androidx.camera.core.CameraSelector
import androidx.camera.core.FocusMeteringAction
import androidx.camera.core.ImageAnalysis
import androidx.camera.core.ImageCapture
import androidx.camera.core.MeteringPoint
import androidx.camera.core.Preview
import androidx.camera.core.resolutionselector.AspectRatioStrategy
import androidx.camera.core.resolutionselector.ResolutionSelector
import androidx.camera.core.resolutionselector.ResolutionStrategy
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.view.PreviewView
import androidx.concurrent.futures.await
import androidx.core.content.ContextCompat
import androidx.lifecycle.LifecycleOwner
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.withContext
import model.FocusPoints
import model.MeteringPointPlatform
import platform.SharedImage
import java.util.concurrent.Executors
import kotlin.math.atan

actual class CameraController(
    val context: Context,
    val lifecycleOwner: LifecycleOwner,
    internal var torchMode: TorchMode,
) {
    private var cameraProvider: ProcessCameraProvider? = null
    private var imageCapture: ImageCapture? = null
    private var preview: Preview? = null
    private var camera: Camera? = null
    var imageAnalyzer: ImageAnalysis? = null
    private var previewView: PreviewView? = null

    private val imageProcessingExecutor = Executors.newFixedThreadPool(2)
    private val memoryManager = MemoryManager
    actual var onImageAvailable: ((SharedImage?) -> Unit)? = null

    fun bindCamera(previewView: PreviewView, onCameraReady: () -> Unit = {}) {
        this.previewView = previewView


        memoryManager.initialize(context)

        val cameraProviderFuture = ProcessCameraProvider.getInstance(context)
        cameraProviderFuture.addListener({
            try {
                cameraProvider = cameraProviderFuture.get()
                cameraProvider?.unbindAll()


                preview = Preview.Builder()
                    .setResolutionSelector(createResolutionSelector())
                    .build()
                    .also {
                        it.surfaceProvider = previewView.surfaceProvider
                    }

                val cameraSelector = CameraSelector.Builder()
                    .requireLensFacing(LENS_FACING_BACK)
                    .build()


                imageAnalyzer = ImageAnalysis.Builder()
                    .setBackpressureStrategy(ImageAnalysis.STRATEGY_KEEP_ONLY_LATEST)
                    .setOutputImageFormat(ImageAnalysis.OUTPUT_IMAGE_FORMAT_RGBA_8888)
                    .build()
                    .also {
                        it.setAnalyzer(imageProcessingExecutor) { image ->
                            val bitmapBuffer =
                                createBitmap(
                                    image.width,
                                    image.height,
                                    Bitmap.Config.ARGB_8888
                                )
                            image.use { bitmapBuffer.copyPixelsFromBuffer(image.planes[0].buffer) }
                            onImageAvailable?.invoke(SharedImage(bitmapBuffer))
                            image.close()
                        }
                    }


                val useCases = mutableListOf(preview!!, imageCapture!!)
                imageAnalyzer?.let { useCases.add(it) }


                camera = cameraProvider?.bindToLifecycle(
                    lifecycleOwner,
                    cameraSelector,
                    *useCases.toTypedArray()
                )

                onCameraReady()

            } catch (exc: Exception) {
                println("Use case binding failed: ${exc.message}")
                exc.printStackTrace()
            }

        }, ContextCompat.getMainExecutor(context))
    }

    private fun createResolutionSelector(): ResolutionSelector {

        memoryManager.updateMemoryStatus()

        return ResolutionSelector.Builder()
            .setResolutionStrategy(ResolutionStrategy.HIGHEST_AVAILABLE_STRATEGY)
            .setAspectRatioStrategy(AspectRatioStrategy.RATIO_4_3_FALLBACK_AUTO_STRATEGY)
            .build()
    }

    actual fun setTorchMode(mode: TorchMode) {
        torchMode = mode
        camera?.cameraControl?.enableTorch(mode == TorchMode.ON)
    }

    actual fun startSession() {
        memoryManager.updateMemoryStatus()
        memoryManager.clearBufferPools()
    }

    actual fun stopSession() {
        cameraProvider?.unbindAll()
        memoryManager.clearBufferPools()
    }

    private fun CameraLens.toCameraXLensFacing(): Int = when (this) {
        CameraLens.FRONT -> CameraSelector.LENS_FACING_FRONT
        CameraLens.BACK -> CameraSelector.LENS_FACING_BACK
    }

    fun cleanup() {
        imageProcessingExecutor.shutdown()
        memoryManager.clearBufferPools()
    }

    actual suspend fun setFocus(focusPoints: FocusPoints): Boolean {

        return withContext(Dispatchers.Main) {
            try {
                val meteringPoint = previewView?.meteringPointFactory!!.createPoint(
                    focusPoints.meteringPoint.x,
                    focusPoints.meteringPoint.y,
                    focusPoints.meteringPoint.size
                )
                val focusMeteringAction = FocusMeteringAction.Builder(meteringPoint).build()
                camera?.cameraControl?.startFocusAndMetering(focusMeteringAction)?.await()
                delay(WAIT_FOR_FOCUS_TO_FINISH)
                true
            } catch (e: Exception) {
                println("Error in focusing: ${e.message}")
                false
            }
        }
    }

    actual fun clearFocus() {
        camera?.cameraControl?.cancelFocusAndMetering()
    }

    actual fun setZoom(zoomRatio: Float) {
        camera?.cameraControl?.setZoomRatio(zoomRatio)
    }

    actual fun clearZoom() {
        camera?.cameraControl?.setZoomRatio(0f)
    }

    actual fun getMinFocusDistance(): Float {
        val cameraManager =
            context.getSystemService(Context.CAMERA_SERVICE) as CameraManager

        try {
            val cameraId = cameraManager.cameraIdList[0]
            val characteristics = cameraManager.getCameraCharacteristics(cameraId)

            val minFocusDistance =
                characteristics.get(CameraCharacteristics.LENS_INFO_MINIMUM_FOCUS_DISTANCE)
            return 1000 / (minFocusDistance ?: 0f)

        } catch (e: CameraAccessException) {
            e.printStackTrace()
        }
        return 0f
    }

    actual fun getFOV(): Double {
        val cameraManager = context.getSystemService(Context.CAMERA_SERVICE) as CameraManager
        val cameraId = cameraManager.cameraIdList[0]
        val cameraCharacteristics = cameraManager.getCameraCharacteristics(cameraId)

        val focalLengths =
            cameraCharacteristics.get(CameraCharacteristics.LENS_INFO_AVAILABLE_FOCAL_LENGTHS)
        val focalLength = focalLengths?.firstOrNull() ?: 0f
        val sensorSize = cameraCharacteristics.get(CameraCharacteristics.SENSOR_INFO_PHYSICAL_SIZE)

        val horizontalFOV = 2 * atan(sensorSize!!.width / (2 * focalLength.toDouble()))
        val verticalFOV = 2 * atan(sensorSize.height / (2 * focalLength.toDouble()))

        val m = Pair(Math.toDegrees(horizontalFOV), Math.toDegrees(verticalFOV))

        return m.first
    }
}

const val WAIT_FOR_FOCUS_TO_FINISH = 2000L

