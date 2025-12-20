package jaw.scene

import androidx.compose.foundation.Image
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
import jaw_generation.jaw.generated.resources.ic_lower_jaw
import jaw_generation.jaw.generated.resources.ic_teeth_01
import jaw_generation.jaw.generated.resources.ic_teeth_02
import jaw_generation.jaw.generated.resources.ic_teeth_03
import jaw_generation.jaw.generated.resources.ic_teeth_04
import jaw_generation.jaw.generated.resources.ic_teeth_05
import jaw_generation.jaw.generated.resources.ic_teeth_06
import jaw_generation.jaw.generated.resources.ic_teeth_07
import jaw_generation.jaw.generated.resources.ic_teeth_08
import jaw_generation.jaw.generated.resources.ic_upper_jaw
import jaw_generation.jaw.generated.resources.teeth_indicator_advanced_1
import jaw_generation.jaw.generated.resources.teeth_indicator_advanced_2
import jaw_generation.jaw.generated.resources.teeth_indicator_advanced_3
import jaw_generation.jaw.generated.resources.teeth_indicator_advanced_4
import jaw_generation.jaw.generated.resources.teeth_indicator_advanced_5
import jaw_generation.jaw.generated.resources.teeth_indicator_advanced_6
import jaw_generation.jaw.generated.resources.teeth_indicator_advanced_7
import jaw_generation.jaw.generated.resources.teeth_indicator_advanced_8
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.ui.tooling.preview.Preview
import kotlin.math.cos
import kotlin.math.sin

val jawWidth = 220.dp
val jawHeight = 220.dp

val rightTeethGroup = listOf(
    ToothSpec(
        id = 17,
        drawable = Res.drawable.ic_teeth_01,
        indicator = Res.drawable.teeth_indicator_advanced_1,
        angleDeg = 0f,
    ),
    ToothSpec(
        18,
        Res.drawable.ic_teeth_02,
        Res.drawable.teeth_indicator_advanced_2,
        12f,
    ),
    ToothSpec(
        19,
        Res.drawable.ic_teeth_03,
        Res.drawable.teeth_indicator_advanced_3,
        23f,
    ),
    ToothSpec(
        20,
        Res.drawable.ic_teeth_04,
        Res.drawable.teeth_indicator_advanced_4,
        35f
    ),
    ToothSpec(
        21,
        Res.drawable.ic_teeth_05,
        Res.drawable.teeth_indicator_advanced_5,
        44f,
    ),
    ToothSpec(
        22,
        Res.drawable.ic_teeth_06,
        Res.drawable.teeth_indicator_advanced_6,
        55f,
    ),
    ToothSpec(
        23,
        Res.drawable.ic_teeth_07,
        Res.drawable.teeth_indicator_advanced_7,
        67f,
    ),
    ToothSpec(
        24,
        Res.drawable.ic_teeth_08,
        Res.drawable.teeth_indicator_advanced_8,
        80f,
        rotation = 170f
    ),
)

val leftTeethGroup = listOf(
    ToothSpec(
        25,
        Res.drawable.ic_teeth_08,
        Res.drawable.teeth_indicator_advanced_8,
        96f,
        rotation = 198f
    ),
    ToothSpec(
        26,
        Res.drawable.ic_teeth_07,
        Res.drawable.teeth_indicator_advanced_7,
        111f,
        rotation = -110f
    ),
    ToothSpec(
        27,
        Res.drawable.ic_teeth_06,
        Res.drawable.teeth_indicator_advanced_6,
        124f,
        rotation = -90f
    ),
    ToothSpec(
        28,
        Res.drawable.ic_teeth_05,
        Res.drawable.teeth_indicator_advanced_5,
        135f,
        rotation = -70f
    ),
    ToothSpec(
        29,
        Res.drawable.ic_teeth_04,
        Res.drawable.teeth_indicator_advanced_4,
        145f,
        rotation = -50f
    ),
    ToothSpec(
        30,
        Res.drawable.ic_teeth_03,
        Res.drawable.teeth_indicator_advanced_3,
        157f,
        rotation = -30f
    ),
    ToothSpec(
        id = 31,
        drawable = Res.drawable.ic_teeth_02,
        indicator = Res.drawable.teeth_indicator_advanced_2,
        angleDeg = 167f,
        rotation = -10f
    ),
    ToothSpec(
        id = 32,
        drawable = Res.drawable.ic_teeth_01,
        indicator = Res.drawable.teeth_indicator_advanced_1,
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
fun JawIllustration() {
    Column(modifier = Modifier.fillMaxSize(), horizontalAlignment = Alignment.CenterHorizontally) {
        Jaw(isUpper = true)
        Jaw()
    }
}

@Composable
fun Jaw(isUpper: Boolean = false) {
    val jawContainerDrawable = if (isUpper) Res.drawable.ic_upper_jaw else Res.drawable.ic_lower_jaw

    BoxWithConstraints(modifier = Modifier.width(jawWidth).height(jawHeight)) {
        val lowerJawTeeth = leftTeethGroup + rightTeethGroup
        val density = LocalDensity.current
        Image(
            painter = painterResource(jawContainerDrawable),
            contentDescription = null,
            modifier = Modifier
                .size(width = jawWidth, height = jawHeight)
                .align(Alignment.Center)
        )
        val radiusX = maxWidth * 0.42f
        val radiusY = maxHeight * 0.72f
        val alignment = if (isUpper) Alignment.BottomCenter else Alignment.TopCenter
        lowerJawTeeth.forEach { tooth ->
            ToothButtonWithIndicator(
                id = tooth.id,
                drawableRes = tooth.drawable,
                indicatorRes = tooth.indicator,
                rotationDegrees = tooth.rotation,
                onToothClicked = {},
                modifier = Modifier
                    .graphicsLayer {
                        if (isUpper)
                            scaleY = -1f
                    }
                    .align(alignment)
                    .padding(top = 22.dp, start = 5.dp)
                    .offset {
                        with(density) {
                            polarOffset(radiusX, radiusY, tooth.angleDeg)
                        }
                    }
            )
        }
    }
}
