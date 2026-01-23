package camera.view.scene.camera

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import camera.di.cameraModule
import camera.view.controller.CameraController
import camera.view.scene.jaw.CameraJawSection
import camera.viewmodel.CameraViewModel
import camera.view.viewmodel.JawViewModel
import shared.model.CameraErrorState
import shared.model.JawSide
import org.jetbrains.compose.resources.stringResource
import org.koin.compose.KoinApplicationPreview
import org.koin.compose.viewmodel.koinViewModel
import shared.resources.Res
import shared.resources.see_result
import shared.theme.Secondary
import shared.theme.White
import shared.theme.appTypography

@Composable
@Preview
fun CameraScenePreview() {
    KoinApplicationPreview(application = { modules(cameraModule) }) {
        CameraScene(null)
    }
}

@Composable
fun CameraScene(
    cameraController: CameraController?,
    cameraViewModel: CameraViewModel = koinViewModel(),
    jawViewModel: JawViewModel = koinViewModel()
) {
    val jawUiState by jawViewModel.uiState.collectAsState()
    val cameraUiState by cameraViewModel.uiState.collectAsState()

    var cameraErrorState by remember { mutableStateOf(CameraErrorState.Ok) }
    var sideError by remember { mutableStateOf(false) }

    var everythingOkToFocus by remember { mutableStateOf<JawSide?>(null) }
    var showResult by remember { mutableStateOf(false) }

    LaunchedEffect(cameraController) {
        cameraController?.let {
            it.onImageAvailable = { image ->
                if (image != null) {
                    cameraViewModel.startImageAnalysis(
                        inputImage = image,
                        jawType = jawUiState.jawType,
                        jawSide = jawUiState.jawSide)
                }
            }
        }
    }
    Box(modifier = Modifier.fillMaxSize()) {
        //Error for distance and angel
        Box(
            modifier = Modifier
                .fillMaxSize()
                .zIndex(3f)
        )


        // Toolbar
        CameraToolbar(
            onBackClick = { },
            onHelpClick = {

            })

        // Jaw section
        CameraJawSection(modifier = Modifier.align(Alignment.TopCenter))

        // See result early
        if (cameraUiState.acceptedTeeth.isNotEmpty())
            SeeResult(cameraViewModel, modifier = Modifier.align(Alignment.BottomCenter))

        // Detection completed
        if (jawUiState.allJawsCompleted)
            ProcessSheet(
                checkupProcessAmount = jawUiState.averageJawsProgress,
                onSeeResultClicked = {},
                onSelectToothClicked = {})
    }
}


@Composable
fun SeeResult(cameraViewModel: CameraViewModel, modifier: Modifier) {
    Button(
        modifier = modifier
            .padding(bottom = 20.dp),
        onClick = {
            cameraViewModel.stopDetecting()
        }, shape = RoundedCornerShape(30.dp),
        colors = ButtonDefaults.buttonColors(Secondary)
    ) {
        Box(
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = stringResource(Res.string.see_result),
                color = White,
                style = appTypography().body14
            )
        }
    }
}












