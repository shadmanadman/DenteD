package jaw.view.viewmodel

import androidx.lifecycle.ViewModel
import jaw.view.contract.SelectionUiState
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import shared.model.ToothNumber
import shared.navigation.CameraNav
import shared.ui.BaseUiEffect

class SelectionViewModel : ViewModel() {

    private val _uiState = MutableStateFlow(SelectionUiState())

    val uiState: StateFlow<SelectionUiState> = _uiState.asStateFlow()

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