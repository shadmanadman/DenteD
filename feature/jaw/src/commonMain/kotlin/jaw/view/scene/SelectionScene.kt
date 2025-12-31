package jaw.scene

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import org.jetbrains.compose.resources.stringResource
import shared.resources.Res
import shared.resources.only_observer_selected_teeth
import shared.theme.appTypography

@Preview
@Composable
fun SelectionScene() {

    Column(modifier = Modifier.fillMaxSize(), horizontalAlignment = Alignment.CenterHorizontally) {
        //Title
        Text(
            modifier = Modifier.fillMaxWidth(),
            textAlign = TextAlign.Center,
            text = stringResource(Res.string.only_observer_selected_teeth),
            style = appTypography().headline26.copy(fontWeight = FontWeight.Bold)
        )

        // Jaw
        JawIllustrationScene()
    }
}