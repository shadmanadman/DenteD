package shared.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.material3.ripple
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import org.jetbrains.compose.resources.StringResource
import org.jetbrains.compose.resources.stringResource
import shared.resources.Res
import shared.resources.app_name
import shared.theme.Black
import shared.theme.appTypography

@Composable
@Preview
fun RgbBorderButtonPreview() {
    RgbBorderButton(text = Res.string.app_name, onClick = {})
}

@Composable
fun RgbBorderButton(
    modifier: Modifier = Modifier,
    text: StringResource,
    backgroundColor: Color = Black.copy(alpha = 0.5f),
    shape: RoundedCornerShape = RoundedCornerShape(12.dp),
    onClick: () -> Unit
) {

    Box(modifier.background(backgroundColor , shape = shape)) {
        // RGB animated border
        AnimatedRgbBorder(
            modifier = Modifier.matchParentSize(),
            shape = shape
        )

        // Button body
        Box(
            modifier = Modifier.clickable(
                interactionSource = remember { MutableInteractionSource() },
                indication = ripple(bounded = true, radius = 20.dp)
            ) {
                onClick()
            }.padding(10.dp).padding(16.dp),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = stringResource(text),
                style = appTypography().headline26.copy(color = Color.White)
            )
        }
    }

}