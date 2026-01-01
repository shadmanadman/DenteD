package view.viewmodel

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.channels.Channel
import shared.navigation.CameraNav
import shared.navigation.MainNav
import shared.ui.BaseUiEffect

class MainViewModel : ViewModel() {
    var effect = Channel<BaseUiEffect>(Channel.UNLIMITED)
        private set

    fun navigateToTeethSelection(){
        effect.trySend(BaseUiEffect.Navigate(MainNav.selection))
    }

    fun navigateBack(){
        effect.trySend(BaseUiEffect.Navigate(MainNav.back))
    }

    fun navigateToDetection(){
        effect.trySend(BaseUiEffect.Navigate(CameraNav.detection))
    }
}