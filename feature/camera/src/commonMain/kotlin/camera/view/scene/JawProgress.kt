package camera.view.scene

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import camera.di.cameraModule
import camera.viewmodel.JawViewModel
import shared.model.JawType
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource
import org.koin.compose.KoinApplicationPreview
import org.koin.compose.viewmodel.koinViewModel
import shared.resources.Res
import shared.resources.ic_half_rectangle
import shared.resources.left
import shared.resources.right
import shared.theme.Transparent
import shared.theme.White
import shared.theme.appTypography

@Composable
@Preview
fun CameraJawSectionPreview() {
    KoinApplicationPreview(application = { modules(cameraModule) }) {
        CameraJawSection(modifier = Modifier)
    }
}

@Composable
fun CameraJawSection(
    jawViewModel: JawViewModel = koinViewModel(),
    modifier: Modifier,
) {
    val currentJaw by jawViewModel.currentJawType.collectAsState()

    Column(modifier = modifier.padding(top = 50.dp)) {
        // Jaw section
        Card(
            modifier = modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(topStart = 21.dp, topEnd = 21.dp))
                .animateContentSize(),
            colors = CardDefaults.cardColors(containerColor = Transparent)
        ) {
            JawToolbar(jawViewModel = jawViewModel)

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 10.dp, bottom = 10.dp), contentAlignment = Alignment.Center
            ) {
                IdentifiedRightAndLeftSideOfUser(
                    modifier = Modifier.align(Alignment.CenterEnd),
                    isLeftSide = true
                )
                AnimatedContent(
                    targetState = currentJaw,
                    transitionSpec = {
                        (fadeIn(animationSpec = tween(500)) + scaleIn(initialScale = 0.9f))
                            .togetherWith(fadeOut(animationSpec = tween(500)) + scaleOut(targetScale = 0.9f))
                    },
                    label = "JawTransition"
                ) { targetJaw ->
                    when (targetJaw) {
                        JawType.UPPER -> UpperJawTeeth(jawViewModel)
                        JawType.LOWER -> LowerJawTeeth(jawViewModel)
                        JawType.FRONT -> FrontTeeth(jawViewModel)
                    }
                }
                IdentifiedRightAndLeftSideOfUser(modifier = Modifier.align(Alignment.CenterStart))
            }

        }
    }

}


@Composable
fun IdentifiedRightAndLeftSideOfUser(modifier: Modifier, isLeftSide: Boolean = false) {
    val rotateForLeftSide = if (isLeftSide)
        180f
    else
        0f

    val rotateTheHalfRectangle = 180f

    val textOffset =
        Modifier.offset(x = (-25).dp, y = 35.dp)


    val text = if (isLeftSide)
        Res.string.left
    else
        Res.string.right

    Box(
        modifier = modifier
            .height(85.dp)
            .padding(start = 32.dp, end = 32.dp)
            .graphicsLayer(rotationZ = rotateForLeftSide)
    ) {
        Image(
            painter = painterResource(Res.drawable.ic_half_rectangle),
            contentDescription = "",
            modifier = Modifier
                .fillMaxHeight()
                .graphicsLayer(rotationZ = rotateTheHalfRectangle)
        )
        Text(
            text = stringResource(text),
            modifier = textOffset
                .graphicsLayer(rotationZ = 90f),
            style = appTypography().title15.copy(fontWeight = FontWeight.Bold),
            color = White
        )
    }
}
