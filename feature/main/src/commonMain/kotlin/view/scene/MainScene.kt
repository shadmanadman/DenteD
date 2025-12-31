package view.scene

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Text
import androidx.compose.material3.ripple
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import core.Route
import io.github.fletchmckee.liquid.LiquidState
import io.github.fletchmckee.liquid.liquefiable
import io.github.fletchmckee.liquid.liquid
import io.github.fletchmckee.liquid.rememberLiquidState
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.receiveAsFlow
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource
import org.koin.compose.KoinApplicationPreview
import org.koin.compose.viewmodel.koinViewModel
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module
import shared.resources.Res
import shared.resources.app_name
import shared.resources.ic_tooth_gradiant
import shared.theme.Transparent
import shared.theme.appTypography
import shared.ui.AnimatedRgbBorder
import shared.ui.BaseUiEffect
import shared.ui.heartBeatScale
import view.viewmodel.MainViewModel

@Preview
@Composable
fun MainScenePreview() {
    KoinApplicationPreview(application = { modules(module { viewModelOf(::MainViewModel) }) }) {
        MainScene(onNavigate = {})
    }
}

@Composable
fun MainScene(
    mainViewModel: MainViewModel = koinViewModel(),
    onNavigate: (Route) -> Unit
) {
    val effects = mainViewModel.effect.receiveAsFlow()

    LaunchedEffect(effects) {
        effects.onEach { effect ->
            when (effect) {
                is BaseUiEffect.InAppMessage -> TODO()
                is BaseUiEffect.Navigate -> onNavigate(effect.route)
            }
        }.collect()
    }
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
        GoButton(
            modifier = Modifier.align(Alignment.Center),
            onClick = mainViewModel::navigateToDetection
        )

        BeSelectiveButton(
            modifier = Modifier.align(Alignment.BottomCenter),
            onClick = mainViewModel::navigateToTeethSelection
        )
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
            modifier = Modifier.align(Alignment.Center).size(size / 2).liquefiable(liquidState)
                .alpha(0.6f)
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
            ) {}
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
                modifier = Modifier.padding(10.dp).padding(16.dp).liquid(liquidState),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "Be Selective",
                    style = appTypography().headline26.copy(color = Color.White)
                )
            }
        }
    }
}