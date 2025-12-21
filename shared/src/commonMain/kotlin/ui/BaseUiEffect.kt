package ui

enum class InAppMessageType {ERROR, WARNING, INFO, SUCCESS}
data class InAppMessageData(val msg: String = "",val type: InAppMessageType)
sealed interface BaseUiEffect {
    data class InAppMessage(val data: InAppMessageData) : BaseUiEffect
    data class Navigation(val route: String) : BaseUiEffect
    data object NavigateBack : BaseUiEffect
}