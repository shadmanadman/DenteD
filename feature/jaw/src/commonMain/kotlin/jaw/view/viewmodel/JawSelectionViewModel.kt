package jaw.view.viewmodel

import androidx.lifecycle.ViewModel
import jaw.view.contract.JawUiState
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import shared.model.ToothNumber
import shared.navigation.CameraNav
import shared.ui.BaseUiEffect

class JawSelectionViewModel : ViewModel() {

    private val _uiState = MutableStateFlow(JawUiState())

    val uiState: StateFlow<JawUiState> = _uiState.asStateFlow()

    var effect = Channel<BaseUiEffect>(Channel.UNLIMITED)
        private set

    fun navigateToDetectionScene() {
        effect.trySend(BaseUiEffect.Navigate(CameraNav.detection))
    }

    fun addSelectedTooth(toothNumber: ToothNumber) {
        _uiState.value.apply {
            if (_uiState.value.selectedToothNumbers.contains(toothNumber))
                selectedToothNumbers.remove(toothNumber)
            else
                selectedToothNumbers.add(toothNumber)
        }
    }
}