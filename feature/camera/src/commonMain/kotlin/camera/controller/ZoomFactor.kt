package com.straiberry.android.checkup.checkup.advanced.presentation.ui.camera.helper

import camera.controller.CameraController
import model.JawSide
import kotlin.math.PI
import kotlin.math.tan


private const val MINIMUM_CODE_SIZE_FOR_LEFT_AND_RIGHT = 8f
private const val MINIMUM_CODE_SIZE_FOR_MIDDLE_SIDE = 18f

object ZoomFactor {

    // Calculate the minimum subject distance based on the camera's field of view
    private fun minimumSubjectDistanceForCode(
        fieldOfView: Float,
        minimumCodeSize: Float = MINIMUM_CODE_SIZE_FOR_LEFT_AND_RIGHT,
        previewFillPercentage: Float = 0.50f
    ): Float {
        val radians = (fieldOfView / 2.0 * (PI/180)).toFloat()
        val filledCodeSize = minimumCodeSize / previewFillPercentage
        return filledCodeSize / tan(radians)
    }

    // Adjust the recommended zoom factor based on the object distance and field of view
    fun setRecommendedZoomFactor(
        cameraController: CameraController,
        jawSide: JawSide
    ) {
        val minimumCodeSize = when (jawSide) {
            JawSide.LEFT -> MINIMUM_CODE_SIZE_FOR_LEFT_AND_RIGHT
            JawSide.RIGHT -> MINIMUM_CODE_SIZE_FOR_LEFT_AND_RIGHT
            JawSide.MIDDLE -> MINIMUM_CODE_SIZE_FOR_MIDDLE_SIDE
        }

        val minimumSubjectDistance = minimumSubjectDistanceForCode(
            fieldOfView = cameraController.getFOV().toFloat(),
            minimumCodeSize = minimumCodeSize,
        )

        val minFocusDistance = cameraController.getMinFocusDistance()
        print("ZoomFactor Minimum focus distance: $minFocusDistance")
        if (minFocusDistance > 0) {
            if (minimumSubjectDistance < minFocusDistance) {
                val zoomFactor = minFocusDistance / minimumSubjectDistance
                cameraController.setZoom(zoomFactor)
            }
        }
    }

}