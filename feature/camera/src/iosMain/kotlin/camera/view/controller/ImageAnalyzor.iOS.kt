package camera.view.controller

import kotlinx.cinterop.ExperimentalForeignApi
import platform.AVFoundation.AVCaptureConnection
import platform.AVFoundation.AVCaptureDevice
import platform.AVFoundation.AVCaptureDeviceInput
import platform.AVFoundation.AVCaptureOutput
import platform.AVFoundation.AVCaptureSession
import platform.AVFoundation.AVCaptureVideoDataOutput
import platform.AVFoundation.AVCaptureVideoDataOutputSampleBufferDelegateProtocol
import platform.CoreImage.CIContext
import platform.CoreImage.CIImage
import platform.CoreImage.createCGImage
import platform.CoreMedia.CMSampleBufferGetImageBuffer
import platform.CoreMedia.CMSampleBufferRef
import platform.CoreVideo.kCVPixelBufferPixelFormatTypeKey
import platform.CoreVideo.kCVPixelFormatType_32BGRA
import platform.UIKit.UIImage
import platform.darwin.NSObject
import platform.darwin.dispatch_get_main_queue

class ImageAnalyzer(
    private val session: AVCaptureSession,
    private val device: AVCaptureDevice,
    private val onFrameAnalyzed: (UIImage) -> Unit
) : NSObject(), AVCaptureVideoDataOutputSampleBufferDelegateProtocol {

    @OptIn(ExperimentalForeignApi::class)
    fun start() {
        val input = AVCaptureDeviceInput.deviceInputWithDevice(device, null) as AVCaptureDeviceInput

        val output = AVCaptureVideoDataOutput().apply {
            // Set delegate and the queue for processing
            setSampleBufferDelegate(this@ImageAnalyzer, dispatch_get_main_queue())

            // Force a specific pixel format (like BGRA)
            videoSettings = mapOf(
                kCVPixelBufferPixelFormatTypeKey to kCVPixelFormatType_32BGRA
            )
        }

        if (session.canAddInput(input)) session.addInput(input)
        if (session.canAddOutput(output)) session.addOutput(output)

        session.startRunning()
    }

    @OptIn(ExperimentalForeignApi::class)
    override fun captureOutput(
        output: AVCaptureOutput,
        didOutputSampleBuffer: CMSampleBufferRef?,
        fromConnection: AVCaptureConnection
    ) {
        val buffer = didOutputSampleBuffer ?: return
        val uiImage = bufferToUIImage(buffer)

        uiImage?.let {
            onFrameAnalyzed(it)
        }
    }

    @OptIn(ExperimentalForeignApi::class)
    private fun bufferToUIImage(sampleBuffer: CMSampleBufferRef): UIImage? {
        val imageBuffer = CMSampleBufferGetImageBuffer(sampleBuffer) ?: return null

        // Convert CIImage -> CGImage -> UIImage
        val ciImage = CIImage.imageWithCVPixelBuffer(imageBuffer)
        val context = CIContext.contextWithOptions(null)
        val cgImage = context.createCGImage(ciImage, ciImage.extent) ?: return null

        return UIImage.imageWithCGImage(cgImage)
    }
}