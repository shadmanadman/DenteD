package jaw.scene

import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import org.jetbrains.compose.resources.stringResource
import shared.resources.left
import shared.resources.lower
import shared.resources.right
import shared.resources.upper
import shared.theme.White
import shared.theme.appTypography

@Preview
@Composable
fun JawGuideLine() {

    ConstraintLayout(
        modifier = Modifier.width(340.dp).height(450.dp)
    ) {
        val (upperTextView, lowerTextView, textViewJawLeft, textViewJawRight) = createRefs()


        Text(
            text = stringResource(shared.resources.Res.string.right),
            style = appTypography().title18.copy(fontWeight = FontWeight.Bold, color = White),
            modifier = Modifier.constrainAs(textViewJawRight) {
                start.linkTo(parent.start, margin = 60.dp)
                top.linkTo(parent.top)
                bottom.linkTo(parent.bottom)
            })

        Text(
            text = stringResource(shared.resources.Res.string.left),
            style = appTypography().title18.copy(fontWeight = FontWeight.Bold, color = White),
            modifier = Modifier.padding(start = 8.dp, end = 8.dp).constrainAs(textViewJawLeft) {
                end.linkTo(parent.end, margin = 60.dp)
                top.linkTo(parent.top)
                bottom.linkTo(parent.bottom)
            })

        Text(
            text = stringResource(shared.resources.Res.string.upper),
            style = appTypography().title18.copy(fontWeight = FontWeight.Bold, color = White)
            , modifier = Modifier.constrainAs(upperTextView) {
                start.linkTo(parent.start)
                end.linkTo(parent.end)
                top.linkTo(parent.top, margin = 115.dp)
            })

        Text(
            text = stringResource(shared.resources.Res.string.lower),
            style = appTypography().title18.copy(fontWeight = FontWeight.Bold, color = White),
            modifier = Modifier.constrainAs(lowerTextView) {
                start.linkTo(parent.start, margin = 10.dp)
                end.linkTo(parent.end)
                bottom.linkTo(parent.bottom, margin = 110.dp)
            })
    }
}
