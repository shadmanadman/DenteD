package jaw.view.contract

import shared.model.ToothNumber

data class JawUiState(val selectedToothNumbers: MutableList<ToothNumber> = mutableListOf())