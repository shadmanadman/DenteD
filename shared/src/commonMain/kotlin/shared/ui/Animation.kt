package shared.ui

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.State
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.center
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.drawOutline
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.drawscope.scale
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.util.lerp
import kotlinx.coroutines.delay
import shared.theme.Accent

@Composable
fun heartBeatScale(): State<Float> {
    val scale = remember { Animatable(1f) }

    LaunchedEffect(Unit) {
        while (true) {
            repeat(2) {
                scale.animateTo(
                    targetValue = 1.45f, animationSpec = tween(500, easing = LinearEasing)
                )
                scale.animateTo(
                    targetValue = 1f, animationSpec = tween(durationMillis = 500, easing = LinearEasing)
                )
            }
            delay(1000)
        }
    }

    return scale.asState()
}


@Composable
fun AnimatedRgbBorder(
    modifier: Modifier = Modifier,
    shape: Shape = CircleShape,
    strokeWidth: Dp = 4.dp,
    reversePulse: Boolean = false,
    hasPulse: Boolean = false
) {
    val infiniteTransition = rememberInfiniteTransition(label = "rgb")
    val hue by infiniteTransition.animateFloat(
        initialValue = 0f, targetValue = 360f, animationSpec = infiniteRepeatable(
            animation = tween(2000, easing = LinearEasing)
        ), label = "hue"
    )

    val infinitePulseTransition = rememberInfiniteTransition(label = "pulse")
    val pulseProgress by infinitePulseTransition.animateFloat(
        initialValue = if (reversePulse) 1f else 0f,
        targetValue = if (reversePulse) 0f else 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(1500, easing = FastOutSlowInEasing)
        ),
        label = "pulseProgress"
    )

    Canvas(modifier = modifier) {
        val strokePx = strokeWidth.toPx()

        val outline = shape.createOutline(size, layoutDirection, this)

        val brush = Brush.sweepGradient(
            colors = listOf(
                Color.hsv(hue, 1f, 1f),
                Color.hsv((hue + 120) % 360, 1f, 1f),
                Color.hsv((hue + 240) % 360, 1f, 1f),
                Color.hsv(hue, 1f, 1f)
            ),
            center = size.center
        )

        if (hasPulse) {
            scale(pulseProgress) {
                drawOutline(
                    outline = outline,
                    brush = brush,
                    style = Stroke(strokePx)
                )
            }
        } else {
            drawOutline(
                outline = outline,
                brush = brush,
                style = Stroke(strokePx)
            )
        }
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