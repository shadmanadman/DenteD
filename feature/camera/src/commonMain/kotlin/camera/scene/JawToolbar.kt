package camera.scene

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
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
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
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import camera.viewmodel.JawViewModel
import resource.DrawableRes
import model.JawType
import org.jetbrains.compose.resources.DrawableResource
import org.jetbrains.compose.resources.painterResource
import theme.Accent
import theme.Secondary
import theme.White
import theme.appTypography


@Composable
fun JawToolbar(
    onJawSelected: (JawType) -> Unit,
    jawViewModel: JawViewModel
) {
    val jawProgress by jawViewModel.jawsProgressDic.collectAsState()
    val selectedJaw by jawViewModel.currentJawType.collectAsState()

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(12.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center
    ) {
        // Jaw buttons
        JawButton(
            jawImage = DrawableRes.lower_jaw,
            jawProgress = jawProgress[JawType.LOWER] ?: 0,
            isSelected = selectedJaw == JawType.LOWER,
            onJawSelected = {
                onJawSelected(JawType.LOWER)
                jawViewModel.changeDetectingJawType(JawType.LOWER)
            },
            isCompleted = jawProgress[JawType.LOWER] == 100
        )
        JawButton(
            jawImage = DrawableRes.front_jaw,
            jawProgress = jawProgress[JawType.FRONT] ?: 0,
            isSelected = selectedJaw == JawType.FRONT,
            onJawSelected = {
                onJawSelected(JawType.FRONT)
                jawViewModel.changeDetectingJawType(JawType.FRONT)
            },
            isCompleted = jawProgress[JawType.FRONT] == 100
        )
        JawButton(
            jawImage = DrawableRes.upper_jaw,
            jawProgress = jawProgress[JawType.UPPER] ?: 0,
            isSelected = selectedJaw == JawType.UPPER,
            onJawSelected = {
                onJawSelected(JawType.UPPER)
                jawViewModel.changeDetectingJawType(JawType.UPPER)
            },
            isCompleted = jawProgress[JawType.UPPER] == 100
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

    // animations
    val alpha by animateFloatAsState(
        targetValue = if (isSelected) 1f else 0f,
        label = ""
    )
    val offsetX by animateDpAsState(targetValue = if (isSelected) 0.dp else (5).dp, label = "")

    val shape = if (isSelected) RoundedCornerShape(15.dp) else CircleShape

    val targetWidth = if (isSelected) 60.dp else 40.dp
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
            .height(40.dp)
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
                .fillMaxSize()
                .padding(start = 4.dp, end = 4.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Image(
                modifier = Modifier
                    .size(21.dp)
                    .offset(x = offsetX),
                painter = painterResource(jawImage),
                colorFilter = ColorFilter.tint(
                    color = jawButtonTintColor(
                        isSelected,
                        isCompleted

                    )
                ),
                contentDescription = "upper jaw button"
            )
            Text(
                modifier = Modifier
                    .padding(start = 4.dp, top = 5.dp)
                    .alpha(alpha),
                text = "$jawProgress%",
                style = appTypography().title15,
                color = Secondary
            )
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