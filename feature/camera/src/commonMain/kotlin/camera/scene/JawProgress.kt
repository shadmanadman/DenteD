package camera.scene

import androidx.compose.animation.animateContentSize
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
import androidx.compose.ui.unit.dp
import camera.viewmodel.JawViewModel
import resource.DrawableRes
import model.JawType
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource
import resource.StringRes
import theme.Transparent
import theme.White
import theme.appTypography

@Composable
fun CameraJawSection(
    jawViewModel: JawViewModel,
    modifier: Modifier,
) {
    val currentJaw by jawViewModel.currentJawType.collectAsState()
    val currentJawSide by jawViewModel.currentJawSide.collectAsState()

    Column(modifier = modifier) {
        // Jaw section
        Card(
            modifier = modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(topStart = 21.dp, topEnd = 21.dp))
                .animateContentSize(),
            colors = CardDefaults.cardColors(containerColor = Transparent)
        ) {
            JawToolbar(
                onJawSelected = { selectedJaw ->
                    jawViewModel.changeDetectingJawType(selectedJaw)
                }, jawViewModel = jawViewModel
            )

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 10.dp, bottom = 10.dp), contentAlignment = Alignment.Center
            ) {
                when (currentJaw) {
                    JawType.UPPER -> UpperJawTeeth(jawViewModel, currentJawSide)
                    JawType.LOWER -> LowerJawTeeth(jawViewModel, currentJawSide)
                    JawType.FRONT -> FrontTeeth(jawViewModel)
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
        StringRes.left
    else
        StringRes.right

    Box(
        modifier = modifier
            .height(85.dp)
            .padding(start = 12.dp, end = 12.dp)
            .graphicsLayer(rotationZ = rotateForLeftSide)
    ) {
        Image(
            painter = painterResource(DrawableRes.ic_half_rectangle),
            contentDescription = "",
            modifier = Modifier
                .fillMaxHeight()
                .graphicsLayer(rotationZ = rotateTheHalfRectangle)
        )
        Text(
            text = stringResource(text),
            modifier = textOffset
                .graphicsLayer(rotationZ = 90f),
            style = appTypography().title15,
            color = White
        )
    }
}
