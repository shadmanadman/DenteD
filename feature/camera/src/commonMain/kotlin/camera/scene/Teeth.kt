package camera.scene

import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.TweenSpec
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.unit.dp
import camera.viewmodel.JawViewModel
import resource.DrawableRes.detected_teeth_stage
import resource.DrawableRes.not_detected_teeth_stage
import ext.toIconIndex
import model.JawSide
import model.ToothDetectionStatus
import org.jetbrains.compose.resources.DrawableResource
import org.jetbrains.compose.resources.painterResource
import theme.Secondary

private const val ROTATE_AROUND = 180f

private const val ANIMATE_DURATION_FOR_JAW_SECTION = 400


@Composable
fun UpperJawTeeth(
    jawViewModel: JawViewModel,
    currentJawSide: JawSide = JawSide.LEFT
) {
    val upperIllustrationTeeth by jawViewModel.upperIllustrationTeeth.collectAsState()
    val updatedListOfTeethIcon = mutableListOf<DrawableResource>()

    upperIllustrationTeeth.forEach {
        when (upperIllustrationTeeth[it.key]) {
            ToothDetectionStatus.INITIAL -> updatedListOfTeethIcon.add(not_detected_teeth_stage[it.key.toIconIndex()])
            ToothDetectionStatus.DETECTED -> updatedListOfTeethIcon.add(not_detected_teeth_stage[it.key.toIconIndex()])
            null -> {}
        }
    }

    Box {
        //JawSplitSection(currentJawSide = currentJawSide)
        LazyRow(
            modifier = Modifier
                .height(50.dp)
                .width(300.dp),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            items(updatedListOfTeethIcon.size) {
                Image(
                    painter = painterResource(updatedListOfTeethIcon[it]),
                    contentDescription = "teeth",
                    modifier = Modifier
                        .graphicsLayer(
                            rotationZ = rotateFirstAndLastTwoTeeth(it),
                            translationY = transactionYForFirstAndLastTeeth(it)
                        )
                        .alpha(0.7f)
                )
            }
        }
    }

}

@Composable
fun LowerJawTeeth(
    jawViewModel: JawViewModel,
    currentJawSide: JawSide = JawSide.LEFT
) {
    val lowerIllustrationTeeth by jawViewModel.lowerIllustrationTeeth.collectAsState()
    val updatedListOfTeethIcon = mutableListOf<DrawableResource>()

    lowerIllustrationTeeth.forEach {
        when (lowerIllustrationTeeth[it.key]) {
            ToothDetectionStatus.INITIAL -> updatedListOfTeethIcon.add(not_detected_teeth_stage[it.key.toIconIndex()])
            ToothDetectionStatus.DETECTED -> updatedListOfTeethIcon.add(detected_teeth_stage[it.key.toIconIndex()])
            null -> {}
        }
    }

    Box {
        //JawSplitSection(currentJawSide = currentJawSide)
        LazyRow(
            modifier = Modifier
                .height(50.dp)
                .width(300.dp),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            items(updatedListOfTeethIcon.size) {
                Image(
                    painter = painterResource(updatedListOfTeethIcon[it]),
                    contentDescription = "teeth",
                    modifier = Modifier
                        .graphicsLayer(
                            rotationZ = rotateFirstAndLastTwoTeeth(it),
                            translationY = -(transactionYForFirstAndLastTeeth(it)),
                            rotationX = ROTATE_AROUND
                        )
                        .alpha(0.7f)
                )
            }
        }
    }

}


@Composable
fun FrontTeeth(jawViewModel: JawViewModel) {
    val frontIllustrationTeeth by jawViewModel.frontIllustrationTeeth.collectAsState()
    val updatedListOfTeethIcon = mutableListOf<DrawableResource>()

    frontIllustrationTeeth.forEach {
        when (frontIllustrationTeeth[it.key]) {
            ToothDetectionStatus.INITIAL -> updatedListOfTeethIcon.add(not_detected_teeth_stage[it.key.toIconIndex()])
            ToothDetectionStatus.DETECTED -> updatedListOfTeethIcon.add(detected_teeth_stage[it.key.toIconIndex()])
            null -> {}
        }
    }

    Box {
        //JawSplitSection(isFrontJaw = true, currentJawSide = JawSide.MIDDLE)

        LazyRow(
            modifier = Modifier
                .height(50.dp)
                .width(300.dp),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            items(updatedListOfTeethIcon.size) {
                Image(
                    painter = painterResource(updatedListOfTeethIcon[it]),
                    contentDescription = "teeth",
                    modifier = Modifier
                        .graphicsLayer(
                            //rotationZ = rotateTheFirstTwoTeethAndTheLastTwoTeeth(it),
                            //translationY = transactionYForFirstAndLastTeeth(it)
                        )
                        .alpha(0.7f)
                )
            }
        }

        LazyRow(
            modifier = Modifier
                .padding(top = 43.dp)
                .width(300.dp)
                .height(50.dp),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            items(updatedListOfTeethIcon.size) {
                Image(
                    painter = painterResource(updatedListOfTeethIcon[it]),
                    contentDescription = "teeth",
                    modifier = Modifier
                        .graphicsLayer(
                            //rotationZ = rotateTheFirstTwoTeethAndTheLastTwoTeeth(it),
                            //translationY = -(transactionYForFirstAndLastTeeth(it)),
                            rotationX = ROTATE_AROUND
                        )
                        .alpha(0.7f)
                )
            }
        }
    }
}


@Composable
fun JawSplitSection(currentJawSide: JawSide = JawSide.LEFT, isFrontJaw: Boolean = false) {
    val firstSectionWidth = 110.dp
    val firstSectionX = 0.dp

    val secondSectionWidth = 88.dp
    val secondSectionX = 108.dp

    val thirdSectionWidth = 105.dp
    val thirdSectionX = 195.dp

    val currentWidth = when (currentJawSide) {
        JawSide.LEFT -> firstSectionWidth
        JawSide.MIDDLE -> secondSectionWidth
        JawSide.RIGHT -> thirdSectionWidth
    }

    val currentX = when (currentJawSide) {
        JawSide.LEFT -> firstSectionX
        JawSide.MIDDLE -> secondSectionX
        JawSide.RIGHT -> thirdSectionX
    }

    val currentHeight = if (isFrontJaw)
        100.dp
    else
        50.dp

    val animatedOffset by animateDpAsState(
        targetValue = currentX,
        animationSpec = TweenSpec(durationMillis = ANIMATE_DURATION_FOR_JAW_SECTION),
        label = "animatedX"
    )
    val animatedWidth by animateDpAsState(
        targetValue = currentWidth,
        animationSpec = TweenSpec(durationMillis = ANIMATE_DURATION_FOR_JAW_SECTION),
        label = "animatedWidth"
    )

    Box(
        modifier = Modifier
            .height(currentHeight)
            .width(animatedWidth)
            .fillMaxWidth()
            .offset(x = animatedOffset)
            .border(BorderStroke(2.dp, color = Secondary))
            .animateContentSize()
    )
}

private fun transactionYForFirstAndLastTeeth(index: Int): Float {
    return if (index == 0 || index == 15)
        -10f
    else
        0f
}

@Composable
private fun rotateFirstAndLastTwoTeeth(index: Int): Float {
    return when (index) {
        0, 1 -> 13f
        14, 15 -> -13f
        else -> 0f
    }
}


