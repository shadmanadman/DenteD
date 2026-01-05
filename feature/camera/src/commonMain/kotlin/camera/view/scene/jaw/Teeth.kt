package camera.view.scene.jaw

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
import shared.ext.toIconIndex
import shared.model.JawSide
import shared.model.ToothDetectionStatus
import org.jetbrains.compose.resources.DrawableResource
import org.jetbrains.compose.resources.painterResource
import org.koin.compose.viewmodel.koinViewModel
import shared.resources.Res
import shared.resources.ic_detected_teeth_1
import shared.resources.ic_detected_teeth_2
import shared.resources.ic_detected_teeth_3
import shared.resources.ic_detected_teeth_4
import shared.resources.ic_detected_teeth_5
import shared.resources.ic_detected_teeth_6
import shared.resources.ic_detected_teeth_7
import shared.resources.ic_detected_teeth_8
import shared.resources.ic_missed_teeth_1
import shared.resources.ic_missed_teeth_2
import shared.resources.ic_missed_teeth_3
import shared.resources.ic_missed_teeth_4
import shared.resources.ic_missed_teeth_5
import shared.resources.ic_missed_teeth_6
import shared.resources.ic_missed_teeth_7
import shared.resources.ic_missed_teeth_8
import shared.resources.ic_teeth_1
import shared.resources.ic_teeth_2
import shared.resources.ic_teeth_3
import shared.resources.ic_teeth_4
import shared.resources.ic_teeth_5
import shared.resources.ic_teeth_6
import shared.resources.ic_teeth_7
import shared.resources.ic_teeth_8
import shared.theme.Secondary

private const val ROTATE_AROUND = 180f

private const val ANIMATE_DURATION_FOR_JAW_SECTION = 400


@Composable
fun UpperJawTeeth(
    jawViewModel: JawViewModel = koinViewModel(),
) {
    val uiState by jawViewModel.uiState.collectAsState()
    val updatedListOfTeethIcon = mutableListOf<DrawableResource>()

    uiState.upperIllustrationTeeth.forEach {
        when (uiState.upperIllustrationTeeth[it.key]) {
            ToothDetectionStatus.INITIAL -> updatedListOfTeethIcon.add(not_detected_teeth_stage[it.key.toIconIndex()])
            ToothDetectionStatus.DETECTED -> updatedListOfTeethIcon.add(detected_teeth_stage[it.key.toIconIndex()])
            ToothDetectionStatus.MISSING -> updatedListOfTeethIcon.add(missing_teeth_stage[it.key.toIconIndex()])
            null ->{}
        }
    }

    Box {
        LazyRow(
            modifier = Modifier
                .height(60.dp)
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
) {
    val uiState by jawViewModel.uiState.collectAsState()
    val updatedListOfTeethIcon = mutableListOf<DrawableResource>()

    uiState.lowerIllustrationTeeth.forEach {
        when (uiState.lowerIllustrationTeeth[it.key]) {
            ToothDetectionStatus.INITIAL -> updatedListOfTeethIcon.add(not_detected_teeth_stage[it.key.toIconIndex()])
            ToothDetectionStatus.DETECTED -> updatedListOfTeethIcon.add(detected_teeth_stage[it.key.toIconIndex()])
            ToothDetectionStatus.MISSING -> updatedListOfTeethIcon.add(missing_teeth_stage[it.key.toIconIndex()])
            null -> {}
        }
    }

    Box {
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
    val uiState by jawViewModel.uiState.collectAsState()
    val updatedListOfTeethIcon = mutableListOf<DrawableResource>()

    uiState.frontIllustrationTeeth.forEach {
        when (uiState.frontIllustrationTeeth[it.key]) {
            ToothDetectionStatus.INITIAL -> updatedListOfTeethIcon.add(not_detected_teeth_stage[it.key.toIconIndex()])
            ToothDetectionStatus.DETECTED -> updatedListOfTeethIcon.add(detected_teeth_stage[it.key.toIconIndex()])
            ToothDetectionStatus.MISSING -> updatedListOfTeethIcon.add(missing_teeth_stage[it.key.toIconIndex()])
            null -> {}
        }
    }

    Box {
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


val not_detected_teeth_stage = listOf(
    Res.drawable.ic_teeth_1,
    Res.drawable.ic_teeth_2,
    Res.drawable.ic_teeth_3,
    Res.drawable.ic_teeth_4,
    Res.drawable.ic_teeth_5,
    Res.drawable.ic_teeth_6,
    Res.drawable.ic_teeth_7,
    Res.drawable.ic_teeth_8,
    Res.drawable.ic_teeth_4,
    Res.drawable.ic_teeth_5,
    Res.drawable.ic_teeth_6,
    Res.drawable.ic_teeth_7,
    Res.drawable.ic_teeth_8,
    Res.drawable.ic_teeth_1,
    Res.drawable.ic_teeth_2,
    Res.drawable.ic_teeth_3,
)
val detected_teeth_stage = listOf(
    Res.drawable.ic_detected_teeth_1,
    Res.drawable.ic_detected_teeth_2,
    Res.drawable.ic_detected_teeth_3,
    Res.drawable.ic_detected_teeth_4,
    Res.drawable.ic_detected_teeth_5,
    Res.drawable.ic_detected_teeth_6,
    Res.drawable.ic_detected_teeth_7,
    Res.drawable.ic_detected_teeth_8,
    Res.drawable.ic_detected_teeth_4,
    Res.drawable.ic_detected_teeth_5,
    Res.drawable.ic_detected_teeth_6,
    Res.drawable.ic_detected_teeth_7,
    Res.drawable.ic_detected_teeth_8,
    Res.drawable.ic_detected_teeth_1,
    Res.drawable.ic_detected_teeth_2,
    Res.drawable.ic_detected_teeth_3,
)

private val missing_teeth_stage = listOf(
    Res.drawable.ic_missed_teeth_1,
    Res.drawable.ic_missed_teeth_2,
    Res.drawable.ic_missed_teeth_3,
    Res.drawable.ic_missed_teeth_4,
    Res.drawable.ic_missed_teeth_5,
    Res.drawable.ic_missed_teeth_6,
    Res.drawable.ic_missed_teeth_7,
    Res.drawable.ic_missed_teeth_8,
    Res.drawable.ic_missed_teeth_4,
    Res.drawable.ic_missed_teeth_5,
    Res.drawable.ic_missed_teeth_6,
    Res.drawable.ic_missed_teeth_7,
    Res.drawable.ic_missed_teeth_8,
    Res.drawable.ic_missed_teeth_1,
    Res.drawable.ic_missed_teeth_2,
    Res.drawable.ic_missed_teeth_3,
)

