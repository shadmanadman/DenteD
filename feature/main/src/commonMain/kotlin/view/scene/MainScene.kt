package view.scene

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Text
import androidx.compose.material3.ripple
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import org.jetbrains.compose.resources.stringResource
import shared.resources.app_name
import shared.theme.Transparent
import shared.theme.appTypography
import shared.ui.AnimatedRgbBorder
import shared.ui.heartBeatScale

@Preview
@Composable
fun MainScenePreview(){
    MainScene(onNavigate = {})
}

@Composable
fun MainScene(onNavigate:(String)->Unit) {

    Box(modifier = Modifier.fillMaxSize().background(Transparent).padding(18.dp)) {
        Text(
            modifier = Modifier.align(Alignment.TopCenter),
            text = stringResource(shared.resources.Res.string.app_name),
            style = appTypography().headline26.copy(
                fontWeight = FontWeight.Bold
            )
        )
        GoButton(modifier = Modifier.align(Alignment.Center)){

        }

        BeSelectiveButton(modifier = Modifier.align(Alignment.BottomCenter)) {

        }
    }
}


@Composable
fun GoButton(
    modifier: Modifier = Modifier,
    size: Dp = 120.dp,
    onClick: () -> Unit
) {
    val scale by heartBeatScale()

    Box(modifier = modifier) {
        // RGB animated border
        AnimatedRgbBorder(
            modifier = Modifier.matchParentSize(),
            hasPulse = true
        )
        AnimatedRgbBorder(
            modifier = Modifier.matchParentSize(),
            reversePulse = true,
            hasPulse = true
        )

        Box(
            modifier = modifier.size(size).scale(scale).clickable(
                interactionSource = remember { MutableInteractionSource() },
                indication = ripple(bounded = true, radius = size / 2)
            ) {
                onClick()
            }, contentAlignment = Alignment.Center
        ) {

            // Button body
            Box(
                modifier = Modifier.size(size - 10.dp).background(
                    color = Color.Black, shape = CircleShape
                ), contentAlignment = Alignment.Center
            ) {
                Text(text = "Go", style = appTypography().headline26.copy(color = Color.White))
            }
        }
    }
}


@Composable
fun BeSelectiveButton(
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) {

    Box(modifier = modifier) {
        // RGB animated border
        AnimatedRgbBorder(
            modifier = Modifier.matchParentSize()
        )

        Box(
            modifier = modifier.clickable(
                interactionSource = remember { MutableInteractionSource() },
                indication = ripple(bounded = true, radius = 20.dp)
            ) {
                onClick()
            }, contentAlignment = Alignment.Center
        ) {

            // Button body
            Box(
                modifier = Modifier.padding(10.dp).background(
                    color = Color.Black, shape = CircleShape
                ).padding(16.dp), contentAlignment = Alignment.Center
            ) {
                Text(text = "Be Selective", style = appTypography().headline26.copy(color = Color.White))
            }
        }
    }
}