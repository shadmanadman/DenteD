package view.scene

import androidx.annotation.Size
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.ripple
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.LinearGradientShader
import androidx.compose.ui.graphics.Shader
import androidx.compose.ui.graphics.ShaderBrush
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import io.github.fletchmckee.liquid.LiquidState
import io.github.fletchmckee.liquid.liquefiable
import io.github.fletchmckee.liquid.liquid
import io.github.fletchmckee.liquid.rememberLiquidState
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource
import shared.resources.Res
import shared.resources.app_name
import shared.resources.ic_lower_jaw
import shared.resources.ic_teeth_1
import shared.resources.ic_tooth_button
import shared.resources.ic_tooth_gradiant
import shared.resources.ic_tooth_shield
import shared.resources.ic_upper_jaw
import shared.theme.Accent
import shared.theme.Black
import shared.theme.Transparent
import shared.theme.White
import shared.theme.appTypography
import shared.ui.AnimatedRgbBorder
import shared.ui.heartBeatScale

@Preview
@Composable
fun MainScenePreview() {
    MainScene(onNavigate = {})
}

@Composable
fun MainScene(onNavigate: (String) -> Unit) {

    Box(
        modifier = Modifier.fillMaxSize().background(Transparent)
            .padding(18.dp)
    ) {

        Text(
            modifier = Modifier.align(Alignment.TopCenter),
            text = stringResource(Res.string.app_name),
            style = appTypography().headline30.copy(
                fontWeight = FontWeight.Bold
            )
        )
        GoButton(modifier = Modifier.align(Alignment.Center)) {

        }

        BeSelectiveButton(modifier = Modifier.align(Alignment.BottomCenter)) {

        }
    }
}


@Composable
fun GoButton(
    modifier: Modifier = Modifier,
    size: Dp = 150.dp,
    liquidState: LiquidState = rememberLiquidState(),
    onClick: () -> Unit
) {
    val scale by heartBeatScale()

    Box(modifier = modifier) {

        // RGB animated border
        AnimatedRgbBorder(
            modifier = Modifier.scale(scale).matchParentSize(),
            hasPulse = false
        )


        Image(
            contentDescription = null,
            painter = painterResource(Res.drawable.ic_tooth_gradiant),
            modifier = Modifier.align(Alignment.Center).size(size/2).liquefiable(liquidState).alpha(0.6f)
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
                modifier = Modifier.size(size - 10.dp)
                    .liquid(liquidState) {
                        curve = 0.85f
                        saturation = 4f
                    }, contentAlignment = Alignment.Center
            ){}
        }
    }
}


@Composable
fun BeSelectiveButton(
    modifier: Modifier = Modifier,
    liquidState: LiquidState = rememberLiquidState(),
    onClick: () -> Unit
) {

    Box(modifier = modifier.liquefiable(liquidState)) {
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
                modifier = Modifier.padding(10.dp).padding(16.dp).liquid(liquidState), contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "Be Selective",
                    style = appTypography().headline26.copy(color = Color.White)
                )
            }
        }
    }
}

@Composable
fun ShaderBackground(
    liquidState: LiquidState,
    modifier: Modifier = Modifier,
) = Box(
    modifier
        .fillMaxSize()
        .liquefiable(liquidState)
        .background(rememberShaderBrush()),
)


@Composable
fun rememberShaderBrush(
    colors: List<Color> = listOf(MaterialTheme.colorScheme.background, MaterialTheme.colorScheme.primary),
): ShaderBrush = remember(colors) {
    object : ShaderBrush() {
        override fun createShader(size: androidx.compose.ui.geometry.Size): Shader = LinearGradientShader(
            colors = colors,
            from = Offset(size.width / 2f, 0f),
            to = Offset(size.width / 2f, size.height),
        )
    }
}
