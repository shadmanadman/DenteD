package jaw.view.scene

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.CircleShape
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
import androidx.compose.ui.unit.dp
import org.jetbrains.compose.resources.DrawableResource
import org.jetbrains.compose.resources.painterResource
import shared.ui.PulseRing


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
    onToothClicked: (ToothSpec) -> Unit
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
                shape = CircleShape
            )
            .clickable(
                interactionSource = null,
                indication = null
            ) {
                isSelected = !isSelected
                onToothClicked(toothSpec)
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

