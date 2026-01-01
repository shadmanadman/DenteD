package camera.controller

import camera.viewmodel.CameraViewModel
import camera.viewmodel.JawViewModel
import shared.ext.convertToJawStatus
import kotlinx.coroutines.delay
import kotlinx.coroutines.suspendCancellableCoroutine
import shared.model.FocusPoints
import shared.model.FocusSection
import shared.model.JawSide
import shared.model.JawType
import shared.model.MeteringPointPlatform
import shared.model.ToothBox
import kotlin.coroutines.resume

object FocusManager {
    private fun changeFocusPoint(
        normalizedPadding: Float,
        visibleBoxes: List<ToothBox>,
        previewWidth: Double,
        previewHeight: Double,
        focusSection: FocusSection
    ): MeteringPointPlatform {
        if (visibleBoxes.isNotEmpty())
            when (focusSection) {
                FocusSection.NEAR -> {

                    val sortedVisibleBoxesByDigit = visibleBoxes.sortedBy { it.x }
                    val item = sortedVisibleBoxesByDigit[0]
                    println("Near right side focus point:${item}")
                    return calculateDevicePoint(
                        normalizedPadding,
                        item,
                        previewWidth, previewHeight
                    )
                }

                FocusSection.MIDDLE -> {

                    val sortedVisibleBoxesByDigit = visibleBoxes.sortedBy { it.x }
                    val itemIndex = sortedVisibleBoxesByDigit.size / 2
                    val item = sortedVisibleBoxesByDigit[itemIndex]
                    println("Middle right side focus point:${item}")

                    return calculateDevicePoint(
                        normalizedPadding,
                        item,
                        previewWidth, previewHeight
                    )
                }

                FocusSection.FARTHEST -> {

                    val sortedVisibleBoxesByDigit = visibleBoxes.sortedBy { it.x }
                    val item = sortedVisibleBoxesByDigit[sortedVisibleBoxesByDigit.lastIndex]
                    println("Farthest right side focus point:${item}")

                    return calculateDevicePoint(
                        normalizedPadding,
                        item,
                        previewWidth, previewHeight
                    )
                }
            }
        else
            return MeteringPointPlatform()
    }


    private fun calculateDevicePoint(
        normalizedPadding: Float,
        item: ToothBox,
        previewWidth: Double,
        previewHeight: Double
    ): MeteringPointPlatform {
        val previewWidth = previewWidth
        val previewHeight = previewHeight

        val cropWidth = 640.0
        val cropHeight = 640.0

        val xOffset = (previewWidth - cropWidth) / 2.0
        val yOffset = (previewHeight - cropHeight) / 2.0

        val dx = normalizedPadding * cropWidth
        val absoluteX = item.x * cropWidth + xOffset + dx
        val absoluteY = item.y * cropHeight + yOffset

        val normalizedX = (absoluteX / previewWidth).coerceIn(0.0, 1.0)
        val normalizedY = (absoluteY / previewHeight).coerceIn(0.0, 1.0)

        println("Focus point:$normalizedX,$normalizedY")
        return MeteringPointPlatform(normalizedX.toFloat(), normalizedY.toFloat())
    }

    private fun focusPointsForUpperAndLowerJaw(
        normalizedPadding: Float,
        visibleBoxes: List<ToothBox>,
        previewWidth: Double,
        previewHeight: Double
    ): List<FocusPoints> {
        return listOf(
            FocusPoints(
                changeFocusPoint(
                    normalizedPadding = normalizedPadding,
                    visibleBoxes = visibleBoxes,
                    previewWidth = previewWidth,
                    previewHeight = previewHeight,
                    focusSection = FocusSection.MIDDLE
                ), FocusSection.MIDDLE
            ),
            FocusPoints(
                changeFocusPoint(
                    normalizedPadding = normalizedPadding,
                    visibleBoxes = visibleBoxes,
                    previewWidth = previewWidth,
                    previewHeight = previewHeight,
                    focusSection = FocusSection.FARTHEST
                ), FocusSection.FARTHEST
            ),
            FocusPoints(
                changeFocusPoint(
                    normalizedPadding = normalizedPadding,
                    visibleBoxes = visibleBoxes,
                    previewWidth = previewWidth,
                    previewHeight = previewHeight,
                    focusSection = FocusSection.NEAR
                ), FocusSection.NEAR
            )
        )
    }

    private fun generateFocusPoint(
        normalizedPadding: Float,
        selectedJawType: JawType,
        jawSide: JawSide,
        visibleBoxes: List<ToothBox>,
        previewHeight: Double,
        previewWidth: Double
    ): List<FocusPoints> {

        if (selectedJawType == JawType.FRONT || jawSide == JawSide.MIDDLE) {
            //resetFocus()
            return emptyList()
        }

        return focusPointsForUpperAndLowerJaw(
            normalizedPadding = normalizedPadding,
            visibleBoxes = visibleBoxes,
            previewWidth = previewWidth,
            previewHeight = previewHeight,
        )
    }


    suspend fun startFocusingSequentially(
        focusPoints: List<FocusPoints>,
        jawViewModel: JawViewModel,
        cameraViewModel: CameraViewModel,
        selectedJawSide: JawSide,
        selectedJawType: JawType
    ) {
        var focusNumber = 0

        jawViewModel.jawSideAnalyzeStarted(
            currentJawSide = convertToJawStatus(
                jawSide = selectedJawSide,
                jawType = selectedJawType
            )
        )

        if (selectedJawSide == JawSide.MIDDLE || focusPoints.isEmpty()) {
            cameraViewModel.changeFocusedSection(FocusSection.MIDDLE)
            delay(WAIT_FOR_FOCUS_TO_FINISH)
            jawViewModel.jawSideAnalyzeCompleted(
                currentJawSide = convertToJawStatus(
                    jawSide = selectedJawSide,
                    jawType = selectedJawType
                )
            )
            return
        }

        while (focusNumber < focusPoints.size) {
            val focusItem = focusPoints[focusNumber]

            blockGoNext()
            println("Focus started :${focusNumber}")

            // Wait for focus to complete
            //val focusSuccess = setFocus(cameraControl, focusItem.meteringPoint)

            if (true) {
                cameraViewModel.changeFocusedSection(focusItem.focusSection)

                goNext()

                println("Focus finished :$focusNumber")
            }
            suspendUntilNext()
            focusNumber++
        }
        jawViewModel.jawSideAnalyzeCompleted(
            currentJawSide = convertToJawStatus(
                jawSide = selectedJawSide,
                jawType = selectedJawType
            )
        )
        cameraViewModel.changeFocusedSection(FocusSection.MIDDLE)
        //resetFocus()
    }

    var proceedToNext = false

    suspend fun suspendUntilNext() {
        suspendCancellableCoroutine { continuation ->
            if (proceedToNext) continuation.resume(Unit)
        }
    }

    private fun blockGoNext() {
        proceedToNext = false
    }

    private fun goNext() {
        proceedToNext = true
    }

    const val WAIT_FOR_FOCUS_TO_FINISH = 2000L
}