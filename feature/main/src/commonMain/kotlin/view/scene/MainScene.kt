package view.scene

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.ui.tooling.preview.Preview
import shared.resources.Res
import shared.resources.app_name
import theme.appTypography


@Composable
fun MainScene() {

}

@Preview
@Composable
fun MainContent() {
    Column(modifier = Modifier.fillMaxSize()) {
        Text(
            modifier = Modifier.fillMaxWidth(),
            textAlign = TextAlign.Center,
            text = stringResource(Res.string.app_name),
            style = appTypography().headline22.copy(fontWeight = FontWeight.Bold)
        )
    }
}