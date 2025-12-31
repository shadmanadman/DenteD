package jaw.scene

import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.draw.scale
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.util.lerp
import org.jetbrains.compose.resources.DrawableResource
import org.jetbrains.compose.resources.painterResource
import shared.theme.Accent


data class ToothSpec(
    val id: Int,
    val drawable: DrawableResource,
    val angleDeg: Float,
    val rotation: Float = 180f,
)

@Composable
fun ToothButtonWithIndicator(
    toothSpec: ToothSpec,
    modifier: Modifier = Modifier,
    onToothClicked: (Int) -> Unit
) {
    var isSelected by remember { mutableStateOf(false) }

    val elevation by animateFloatAsState(
        targetValue = if (isSelected) 15.dp.value else 0.dp.value,
        animationSpec = tween(durationMillis = 300)
    )
    val scale by animateFloatAsState(
        targetValue = if (isSelected) 1.1f else 1f,
        animationSpec = tween(durationMillis = 300)
    )




    Box(
        modifier = modifier
            .wrapContentSize()
            .rotate(toothSpec.rotation)
            .scale(scale)
            .shadow(
                elevation = elevation.dp,
                shape = androidx.compose.foundation.shape.CircleShape
            )
            .clickable(
                interactionSource = null,
                indication = null
            ) {
                isSelected = !isSelected
                onToothClicked(toothSpec.id)
            }
    ) {


        Image(
            painter = painterResource(toothSpec.drawable),
            contentDescription = "Teeth ${toothSpec.id} Button",
        )

        PulseRing(
            modifier = Modifier
                .size(20.dp)
                .align(Alignment.Center),
            isActive = isSelected,
        )
    }

}

@Composable
fun PulseRing(
    modifier: Modifier = Modifier,
    isActive: Boolean,
    color: Color = Accent,
    maxRadiusFraction: Float = 0.9f,
    strokeWidth: Dp = 3.dp
) {
    if (!isActive) return

    val infiniteTransition = rememberInfiniteTransition(label = "pulse")

    val progress by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(
                durationMillis = 1500,
                easing = FastOutSlowInEasing
            )
        ),
        label = "pulseProgress"
    )

    Canvas(modifier = modifier) {
        val radius = lerp(
            0f ,
            maxRadiusFraction * size.minDimension,
            progress
        )

        drawCircle(
            color = color.copy(alpha = 1f - progress),
            radius = radius,
            style = Stroke(width = strokeWidth.toPx())
        )
    }
}