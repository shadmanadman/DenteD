package jaw.view.scene

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import core.Route
import jaw.di.jawModule
import jaw.view.viewmodel.JawSelectionViewModel
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.receiveAsFlow
import org.jetbrains.compose.resources.stringResource
import org.koin.compose.KoinApplicationPreview
import org.koin.compose.viewmodel.koinViewModel
import shared.resources.Res
import shared.resources.be_selective
import shared.resources.done
import shared.resources.only_observer_selected_teeth
import shared.theme.appTypography
import shared.ui.BaseUiEffect
import shared.ui.RgbBorderButton

@Preview
@Composable
fun SelectionScenePreview() {
    KoinApplicationPreview(application = { modules(jawModule) }) {
        SelectionScene(onNavigate = {})
    }
}


@Composable
fun SelectionScene(
    jawSelectionViewModel: JawSelectionViewModel = koinViewModel(),
    onNavigate: (Route) -> Unit
) {
    val effects = jawSelectionViewModel.effect.receiveAsFlow()

    LaunchedEffect(effects) {
        effects.onEach { effect ->
            when (effect) {
                is BaseUiEffect.InAppMessage -> TODO()
                is BaseUiEffect.Navigate -> onNavigate(effect.route)
            }
        }.collect()
    }

    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        //Title
        Text(
            modifier = Modifier.fillMaxWidth(),
            textAlign = TextAlign.Center,
            text = stringResource(Res.string.only_observer_selected_teeth),
            style = appTypography().headline26.copy(fontWeight = FontWeight.Bold)
        )

        // Jaw
        JawIllustrationScene(onToothClicked = jawSelectionViewModel::addSelectedTooth)

        // Done
        RgbBorderButton(
            text = Res.string.done,
            onClick = jawSelectionViewModel::navigateToDetectionScene
        )
    }
}