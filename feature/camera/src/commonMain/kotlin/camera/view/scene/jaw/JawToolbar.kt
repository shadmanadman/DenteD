package camera.view.scene.jaw

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import camera.di.cameraModule
import camera.viewmodel.JawViewModel
import shared.model.JawType
import org.jetbrains.compose.resources.DrawableResource
import org.jetbrains.compose.resources.painterResource
import org.koin.compose.KoinApplicationPreview
import org.koin.compose.viewmodel.koinViewModel
import shared.resources.Res
import shared.resources.ic_front_jaw
import shared.resources.ic_lower_jaw
import shared.resources.ic_upper_jaw
import shared.theme.Accent
import shared.theme.Secondary
import shared.theme.White
import shared.theme.appTypography

@Composable
@Preview
fun JawToolbarPreview() {
    KoinApplicationPreview(application = { modules(cameraModule) }) {
        JawToolbar()
    }
}

@Composable
fun JawToolbar(
    jawViewModel: JawViewModel = koinViewModel()
) {
    val uiState by jawViewModel.uiState.collectAsState()

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(12.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center
    ) {
        JawButton(
            jawImage = Res.drawable.ic_lower_jaw,
            jawProgress = uiState.jawProgress[JawType.LOWER] ?: 0,
            isSelected = uiState.jawType == JawType.LOWER,
            onJawSelected = {
                jawViewModel.changeDetectingJawType(JawType.LOWER)
            },
            isCompleted = uiState.jawProgress[JawType.LOWER] == 100
        )
        JawButton(
            jawImage = Res.drawable.ic_front_jaw,
            jawProgress = uiState.jawProgress[JawType.FRONT] ?: 0,
            isSelected = uiState.jawType == JawType.FRONT,
            onJawSelected = {
                jawViewModel.changeDetectingJawType(JawType.FRONT)
            },
            isCompleted = uiState.jawProgress[JawType.FRONT] == 100
        )
        JawButton(
            jawImage = Res.drawable.ic_upper_jaw,
            jawProgress = uiState.jawProgress[JawType.UPPER] ?: 0,
            isSelected = uiState.jawType == JawType.UPPER,
            onJawSelected = {
                jawViewModel.changeDetectingJawType(JawType.UPPER)
            },
            isCompleted = uiState.jawProgress[JawType.UPPER] == 100
        )

    }
}


@Composable
fun JawButton(
    jawImage: DrawableResource,
    jawProgress: Int,
    isSelected: Boolean,
    onJawSelected: () -> Unit,
    isCompleted: Boolean
) {

    val alpha by animateFloatAsState(
        targetValue = if (isSelected) 1f else 0f,
        label = ""
    )

    val shape = if (isSelected) RoundedCornerShape(15.dp) else CircleShape

    val targetWidth = if (isSelected) 80.dp else 60.dp
    val animatedWidth by animateDpAsState(targetValue = targetWidth, label = "")

    Box(
        modifier = Modifier
            .clickable(
                onClick = {
                    if (isCompleted.not())
                        onJawSelected()
                },
                indication = null,
                interactionSource = remember { MutableInteractionSource() })
            .padding(end = 8.dp)
            .width(animatedWidth)
            .height(60.dp)
            .shadow(elevation = jawButtonShadow(isSelected), shape = RoundedCornerShape(10.dp))
            .clip(shape = shape)
            .background(color = White)
            .border(
                width = 1.dp,
                color = jawButtonBorderColor(isSelected, isCompleted),
                shape = shape
            )
    ) {
        Row(
            modifier = Modifier
                .align(Alignment.Center),
        ) {
            Image(
                modifier = Modifier
                    .size(30.dp),
                painter = painterResource(jawImage),
                colorFilter = ColorFilter.tint(
                    color = jawButtonTintColor(
                        isSelected,
                        isCompleted
                    )
                ),
                contentDescription = "upper jaw button"
            )
            AnimatedVisibility(isSelected) {
                Text(
                    modifier = Modifier
                        .padding(start = 4.dp, top = 5.dp)
                        .alpha(alpha),
                    text = "$jawProgress%",
                    style = appTypography().title18,
                    color = Secondary
                )
            }

        }
    }
}


private fun jawButtonBorderColor(isSelected: Boolean, isCompleted: Boolean): Color {
    return if (isSelected || isCompleted)
        Secondary
    else
        White
}

private fun jawButtonShadow(isSelected: Boolean): Dp {
    return if (isSelected)
        5.dp
    else
        0.dp
}

private fun jawButtonTintColor(isSelected: Boolean, isCompleted: Boolean): Color {
    return if (isSelected || isCompleted)
        Secondary
    else
        Accent
}