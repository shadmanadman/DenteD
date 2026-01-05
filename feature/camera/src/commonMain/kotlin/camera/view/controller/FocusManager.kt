package camera.view.controller

import kotlinx.coroutines.channels.Channel
import shared.ext.convertToJawStatus
import shared.model.FocusPoints
import shared.model.FocusSection
import shared.model.JawSide
import shared.model.JawSideStatus
import shared.model.JawType
import shared.model.MeteringPointPlatform
import shared.model.ToothBox

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

    private val nextSignal = Channel<Unit>(capacity = Channel.RENDEZVOUS)

    suspend fun goNext() {
        nextSignal.send(Unit)
    }

    suspend fun startFocusingSequentially(
        focusPoints: List<FocusPoints>,
        selectedJawSide: JawSide,
        selectedJawType: JawType,
        onSideAnalyzeStarted: (JawSideStatus) -> Unit,
        onSideAnalyzeCompleted: (JawSideStatus) -> Unit,
        onFocusSectionChanged: (FocusSection) -> Unit
    ) {
        var focusNumber = 0

        onSideAnalyzeStarted(
            convertToJawStatus(
                jawSide = selectedJawSide,
                jawType = selectedJawType
            )
        )

        if (selectedJawSide == JawSide.MIDDLE || focusPoints.isEmpty()) {
            onFocusSectionChanged(FocusSection.MIDDLE)
            //delay(WAIT_FOR_FOCUS_TO_FINISH)
            //resetFocus()
            onSideAnalyzeCompleted(
                convertToJawStatus(
                    jawSide = selectedJawSide,
                    jawType = selectedJawType
                )
            )
            return
        }

        while (focusNumber < focusPoints.size) {
            val focusItem = focusPoints[focusNumber]

            nextSignal.receive()
            println("Focus started :${focusNumber}")

            // Wait for focus to complete
            //val focusSuccess = setFocus(cameraControl, focusItem.meteringPoint)

            if (true) {
                onFocusSectionChanged(focusItem.focusSection)

                goNext()

                println("Focus finished :$focusNumber")
            }
            focusNumber++
        }
        onSideAnalyzeCompleted(
            convertToJawStatus(
                jawSide = selectedJawSide,
                jawType = selectedJawType
            )
        )
        onFocusSectionChanged(FocusSection.MIDDLE)
        //resetFocus()
    }

    const val WAIT_FOR_FOCUS_TO_FINISH = 2000L
}