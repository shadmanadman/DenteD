package jaw.scene

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
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

val jawRadiosX = 255.dp
val jawRadiosY = 890.dp

val lowerRightTeethGroup = listOf(
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

val lowerLeftTeethGroup = listOf(
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


fun polarOffset(radiusX: Dp, radiusY: Dp, angle: Float): IntOffset {
    val rad = angle.toRadians()

    return IntOffset(
        x = (radiusX.value * cos(rad)).toInt(),
        y = (radiusY.value / 2 * sin(rad)).toInt()
    )
}


@Preview
@Composable
fun LowerJaw() {
    Box(
        modifier = Modifier.fillMaxWidth()
    ) {
        val lowerJawTeeth = lowerLeftTeethGroup + lowerRightTeethGroup
        Image(
            painter = painterResource(Res.drawable.ic_lower_jaw),
            contentDescription = null,
            modifier = Modifier
                .size(width = jawWidth, height = jawHeight)
                .align(Alignment.Center)
        )

        lowerJawTeeth.forEach { tooth ->
            ToothButtonWithIndicator(
                id = tooth.id,
                drawableRes = tooth.drawable,
                indicatorRes = tooth.indicator,
                rotationDegrees = tooth.rotation,
                onToothClicked = {},
                modifier = Modifier
                    .align(Alignment.TopCenter)
                    .padding(top = 22.dp, start = 5.dp)
                    .offset { polarOffset(tooth.radiusXDp, tooth.radiusYDp, tooth.angleDeg) }
            )
        }
    }
}