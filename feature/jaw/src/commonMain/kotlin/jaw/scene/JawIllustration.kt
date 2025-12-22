package jaw.scene

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import ext.toRadians
import jaw_generation.jaw.generated.resources.Res
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.ui.tooling.preview.Preview
import resource.DrawableRes
import kotlin.math.cos
import kotlin.math.sin

val jawIllustrationSize = 220.dp

val rightTeethGroup = listOf(
    ToothSpec(
        id = 17,
        drawable = DrawableRes.tooth_illustration_1,
        angleDeg = 0f,
    ),
    ToothSpec(
        id = 18,
        drawable = DrawableRes.tooth_illustration_2,
        angleDeg = 12f,
    ),
    ToothSpec(
        id = 19,
        drawable = DrawableRes.tooth_illustration_3,
        angleDeg = 23f,
    ),
    ToothSpec(
        id = 20,
        drawable = DrawableRes.tooth_illustration_4,
        angleDeg = 35f
    ),
    ToothSpec(
        id = 21,
        drawable = DrawableRes.tooth_illustration_5,
        angleDeg = 44f,
    ),
    ToothSpec(
        id = 22,
        drawable = DrawableRes.tooth_illustration_6,
        angleDeg = 55f,
    ),
    ToothSpec(
        id = 23,
        drawable = DrawableRes.tooth_illustration_7,
        angleDeg = 67f,
    ),
    ToothSpec(
        id = 24,
        drawable = DrawableRes.tooth_illustration_8,
        angleDeg = 80f,
        rotation = 170f
    ),
)

val leftTeethGroup = listOf(
    ToothSpec(
        id = 25,
        drawable = DrawableRes.tooth_illustration_8,
        angleDeg = 96f,
        rotation = 198f
    ),
    ToothSpec(
        id = 26,
        drawable = DrawableRes.tooth_illustration_7,
        angleDeg = 111f,
        rotation = -110f
    ),
    ToothSpec(
        id = 27,
        drawable = DrawableRes.tooth_illustration_6,
        angleDeg = 124f,
        rotation = -90f
    ),
    ToothSpec(
        id = 28,
        drawable = DrawableRes.tooth_illustration_5,
        angleDeg = 135f,
        rotation = -70f
    ),
    ToothSpec(
        id = 29,
        drawable = DrawableRes.tooth_illustration_4,
        angleDeg = 145f,
        rotation = -50f
    ),
    ToothSpec(
        id = 30,
        drawable = DrawableRes.tooth_illustration_3,
        angleDeg = 157f,
        rotation = -30f
    ),
    ToothSpec(
        id = 31,
        drawable = DrawableRes.tooth_illustration_2,
        angleDeg = 167f,
        rotation = -10f
    ),
    ToothSpec(
        id = 32,
        drawable = DrawableRes.tooth_illustration_1,
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
        if (isUpper) DrawableRes.upper_jaw_illustration else DrawableRes.lower_jaw_illustration
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
