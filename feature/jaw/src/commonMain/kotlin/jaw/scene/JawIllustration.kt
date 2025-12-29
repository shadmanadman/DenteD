package jaw.scene

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import shared.ext.toRadians

import org.jetbrains.compose.resources.painterResource
import shared.resources.Res
import shared.resources.ic_lower_jaw
import shared.resources.ic_teeth_01
import shared.resources.ic_teeth_02
import shared.resources.ic_teeth_03
import shared.resources.ic_teeth_04
import shared.resources.ic_teeth_05
import shared.resources.ic_teeth_06
import shared.resources.ic_teeth_07
import shared.resources.ic_teeth_08
import shared.resources.ic_upper_jaw

import kotlin.math.cos
import kotlin.math.sin

val jawIllustrationSize = 220.dp

val rightTeethGroup = listOf(
    ToothSpec(
        id = 1,
        drawable = Res.drawable.ic_teeth_01,
        angleDeg = 0f,
    ),
    ToothSpec(
        id = 2,
        drawable = Res.drawable.ic_teeth_02,
        angleDeg = 12f,
    ),
    ToothSpec(
        id = 3,
        drawable = Res.drawable.ic_teeth_03,
        angleDeg = 23f,
    ),
    ToothSpec(
        id = 4,
        drawable = Res.drawable.ic_teeth_04,
        angleDeg = 35f
    ),
    ToothSpec(
        id = 5,
        drawable = Res.drawable.ic_teeth_05,
        angleDeg = 44f,
    ),
    ToothSpec(
        id = 6,
        drawable = Res.drawable.ic_teeth_06,
        angleDeg = 55f,
    ),
    ToothSpec(
        id = 7,
        drawable = Res.drawable.ic_teeth_07,
        angleDeg = 67f,
    ),
    ToothSpec(
        id = 8,
        drawable = Res.drawable.ic_teeth_08,
        angleDeg = 80f,
        rotation = 170f
    ),
)

val leftTeethGroup = listOf(
    ToothSpec(
        id = 9,
        drawable = Res.drawable.ic_teeth_08,
        angleDeg = 96f,
        rotation = 198f
    ),
    ToothSpec(
        id = 10,
        drawable = Res.drawable.ic_teeth_07,
        angleDeg = 111f,
        rotation = -110f
    ),
    ToothSpec(
        id = 11,
        drawable = Res.drawable.ic_teeth_06,
        angleDeg = 124f,
        rotation = -90f
    ),
    ToothSpec(
        id = 12,
        drawable = Res.drawable.ic_teeth_05,
        angleDeg = 135f,
        rotation = -70f
    ),
    ToothSpec(
        id = 13,
        drawable = Res.drawable.ic_teeth_04,
        angleDeg = 145f,
        rotation = -50f
    ),
    ToothSpec(
        id = 14,
        drawable = Res.drawable.ic_teeth_03,
        angleDeg = 157f,
        rotation = -30f
    ),
    ToothSpec(
        id = 15,
        drawable = Res.drawable.ic_teeth_02,
        angleDeg = 167f,
        rotation = -10f
    ),
    ToothSpec(
        id = 16,
        drawable = Res.drawable.ic_teeth_01,
        angleDeg = 180f,
        rotation = 0f
    ),
)


fun Density.polarOffset(radiusX: Dp, radiusY: Dp, angle: Float): IntOffset {
    val rad = angle.toRadians()

    return IntOffset(
        x = (radiusX.roundToPx() * cos(rad)).toInt(),
        y = (radiusY.roundToPx() * sin(rad)).toInt()
    )
}

@Preview
@Composable
fun JawIllustrationScene() {
    Box(contentAlignment = Alignment.Center) {
        Column {
            Jaw(isUpper = true)
            Jaw()
        }
        JawGuideLine()
    }
}

@Composable
fun Jaw(isUpper: Boolean = false) {
    val jawContainerDrawable =
        if (isUpper) Res.drawable.ic_upper_jaw else Res.drawable.ic_lower_jaw
    val alignment = if (isUpper) Alignment.BottomCenter else Alignment.TopCenter
    val startPadding = if (isUpper) 0.dp else 6.dp
    BoxWithConstraints(modifier = Modifier.size(jawIllustrationSize)) {

        Image(
            painter = painterResource(jawContainerDrawable),
            contentDescription = null,
            modifier = Modifier
                .size(jawIllustrationSize)
                .align(Alignment.Center)
        )

        val radiusX = maxWidth * 0.42f
        val radiusY = maxHeight * 0.72f
        val lowerJawTeeth = leftTeethGroup + rightTeethGroup

        val density = LocalDensity.current

        lowerJawTeeth.forEach { tooth ->
            ToothButtonWithIndicator(
                toothSpec = tooth,
                onToothClicked = {},
                modifier = Modifier
                    .graphicsLayer {
                        if (isUpper)
                            scaleY = -1f
                    }
                    .align(alignment)
                    .padding(top = 20.dp, start = startPadding)
                    .offset {
                        with(density) {
                            polarOffset(radiusX, radiusY, tooth.angleDeg)
                        }
                    }
            )
        }
    }
}
