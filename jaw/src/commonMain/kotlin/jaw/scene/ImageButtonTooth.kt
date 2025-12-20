package jaw.scene

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material3.ripple
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.draw.scale
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.unit.dp
import org.jetbrains.compose.resources.DrawableResource
import org.jetbrains.compose.resources.painterResource


data class ToothSpec(
    val id: Int,
    val drawable: DrawableResource,
    val indicator: DrawableResource,
    val angleDeg: Float,
    val rotation: Float = 180f,
)

@Composable
fun ToothButtonWithIndicator(
    id: Int,
    drawableRes: DrawableResource,
    indicatorRes: DrawableResource,
    rotationDegrees: Float = 0f,
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
            .rotate(rotationDegrees)
            .scale(scale)
            .shadow(
                elevation = elevation.dp,
                shape = androidx.compose.foundation.shape.CircleShape
            )
            .clickable(interactionSource = remember { MutableInteractionSource() },
                indication = ripple(bounded = true, radius = 40.dp)) {
                isSelected = !isSelected
                onToothClicked(id)
            }
    ) {
        Image(
            painter = painterResource(drawableRes),
            contentDescription = "Teeth $id Button",
        )
    }
}