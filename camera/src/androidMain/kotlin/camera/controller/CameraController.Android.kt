package camera.controller

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Bitmap.createBitmap
import android.graphics.Color
import android.hardware.camera2.CameraMetadata.LENS_FACING_BACK
import android.provider.ContactsContract
import androidx.camera.core.Camera
import androidx.camera.core.CameraSelector
import androidx.camera.core.ImageAnalysis
import androidx.camera.core.ImageCapture
import androidx.camera.core.Preview
import androidx.camera.core.resolutionselector.AspectRatioStrategy
import androidx.camera.core.resolutionselector.ResolutionSelector
import androidx.camera.core.resolutionselector.ResolutionStrategy
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.view.PreviewView
import androidx.core.content.ContextCompat
import androidx.core.graphics.scale
import androidx.lifecycle.LifecycleOwner
import java.nio.ByteBuffer
import java.nio.ByteOrder
import java.util.concurrent.Executors
import kotlin.use
import androidx.core.graphics.get
import detector.INPUT_IMAGE_SIZE
import platform.SharedImage

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

}
