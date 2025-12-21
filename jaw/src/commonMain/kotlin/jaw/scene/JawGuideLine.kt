package jaw.scene

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import jaw_generation.jaw.generated.resources.Res
import jaw_generation.jaw.generated.resources.ic_guide_line
import jaw_generation.jaw.generated.resources.ic_guide_line_vertical
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.ui.tooling.preview.Preview
import theme.Black
import theme.Secondary
import theme.White
import theme.appTypography

@Preview
@Composable
fun JawGuideLine() {

    ConstraintLayout(
        modifier = Modifier.width(340.dp).height(450.dp)
    ) {
        val (upperTextView, lowerTextView, textViewJawLeft, textViewJawRight) = createRefs()


        Text(
            text = "Right",
            style = appTypography().title18.copy(fontWeight = FontWeight.Bold, color = White),
            modifier = Modifier.constrainAs(textViewJawRight) {
                start.linkTo(parent.start, margin = 60.dp)
                top.linkTo(parent.top)
                bottom.linkTo(parent.bottom)
            })

        Text(
            text = "Left",
            style = appTypography().title18.copy(fontWeight = FontWeight.Bold, color = White),
            modifier = Modifier.padding(start = 8.dp, end = 8.dp).constrainAs(textViewJawLeft) {
                end.linkTo(parent.end, margin = 60.dp)
                top.linkTo(parent.top)
                bottom.linkTo(parent.bottom)
            })

        Text(
            text = "Upper",
            style = appTypography().title18.copy(fontWeight = FontWeight.Bold, color = White)
            , modifier = Modifier.constrainAs(upperTextView) {
                start.linkTo(parent.start)
                end.linkTo(parent.end)
                top.linkTo(parent.top, margin = 115.dp)
            })

        Text(
            text = "Lower",
            style = appTypography().title18.copy(fontWeight = FontWeight.Bold, color = White),
            modifier = Modifier.constrainAs(lowerTextView) {
                start.linkTo(parent.start, margin = 10.dp)
                end.linkTo(parent.end)
                bottom.linkTo(parent.bottom, margin = 110.dp)
            })
    }
}
