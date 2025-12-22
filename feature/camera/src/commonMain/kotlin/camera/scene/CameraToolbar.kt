package camera.scene

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import resource.DrawableRes
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource
import resource.StringRes
import theme.Secondary
import theme.White
import theme.appTypography

@Composable
fun CameraToolbar(onBackClick: () -> Unit, onHelpClick: () -> Unit) {

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .zIndex(1f)
            .padding(top = 12.dp, start = 20.dp, end = 20.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        // Close the camera
        Row(
            modifier = Modifier
                .weight(1f),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Image(
                painter = painterResource(DrawableRes.close),
                colorFilter = ColorFilter.tint(
                    White
                ),
                contentDescription = "Showing the info about camera"
            )


            Text(
                modifier = Modifier
                    .padding(start = 4.dp)
                    .clickable(onClick = { onBackClick() },
                        indication = null,
                        interactionSource = remember { MutableInteractionSource() }),
                text = stringResource(StringRes.close),
                style = appTypography().title15,
                color = White
            )
        }


        // The camera info button
        Image(
            modifier = Modifier
                .clickable(onClick = { onHelpClick() },
                    indication = null,
                    interactionSource = remember { MutableInteractionSource() })
                .padding(end = 24.dp),
            painter = painterResource(DrawableRes.info),
            colorFilter = ColorFilter.tint(
                Secondary
            ),
            contentDescription = "Help Button"
        )

        // The torch is on for camera
        Image(
            painter = painterResource(DrawableRes.lightening), colorFilter = ColorFilter.tint(
                Color.White
            ), contentDescription = "Torch is on"
        )
    }
}