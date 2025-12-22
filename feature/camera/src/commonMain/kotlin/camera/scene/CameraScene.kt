package camera.scene

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import camera.viewmodel.AnalyzerViewModel
import camera.viewmodel.CameraViewModel
import camera.viewmodel.JawViewModel
import model.CameraErrorState
import model.JawSide
import org.jetbrains.compose.resources.stringResource
import resource.StringRes
import theme.Secondary
import theme.White
import theme.appTypography

@Composable
fun CameraScreen(
    analyzerViewModel: AnalyzerViewModel,
    cameraViewModel: CameraViewModel,
    jawViewModel: JawViewModel
) {
    val selectedJawType by jawViewModel.currentJawType.collectAsState()
    val selectedJawSide by jawViewModel.currentJawSide.collectAsState()

    val acceptedTeeth by cameraViewModel.acceptedTeeth.collectAsState()
    val acceptedFrames by cameraViewModel.acceptedFrames.collectAsState()

    val allJawsCompleted by jawViewModel.allJawsCompleted.collectAsState()

    var cameraErrorState by remember { mutableStateOf(CameraErrorState.Ok) }
    var sideError by remember { mutableStateOf(false) }

    var everythingOkToFocus by remember { mutableStateOf<JawSide?>(null) }
    var showResult by remember { mutableStateOf(false) }

    Box(modifier = Modifier.fillMaxSize()) {
        //Error for distance and angel
        Box(
            modifier = Modifier
                .fillMaxSize()
                .zIndex(3f)
                .background(color = Secondary)
        )


        // Toolbar
        CameraToolbar(
            onBackClick = { },
            onHelpClick = {

            })

        // Jaw section
        CameraJawSection(
            jawViewModel = jawViewModel,
            modifier = Modifier.align(Alignment.TopCenter)
        )

        // See result early
        if (acceptedTeeth.isNotEmpty())
            SeeResult(cameraViewModel, modifier = Modifier.align(Alignment.BottomCenter))

        // Detection completed
        if (allJawsCompleted)
            ProcessSheet(
                jawViewModel.averageJawsProgress(),
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
                text = stringResource(StringRes.see_result),
                color = White,
                style = appTypography().body14
            )
        }
    }
}












