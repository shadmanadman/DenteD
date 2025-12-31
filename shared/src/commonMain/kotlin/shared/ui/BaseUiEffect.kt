package shared.ui

import core.Route

enum class InAppMessageType {ERROR, WARNING, INFO, SUCCESS}
data class InAppMessageData(val msg: String = "",val type: InAppMessageType)
sealed interface BaseUiEffect {
    data class InAppMessage(val data: InAppMessageData) : BaseUiEffect
    data class Navigate(val route: Route) : BaseUiEffect
}