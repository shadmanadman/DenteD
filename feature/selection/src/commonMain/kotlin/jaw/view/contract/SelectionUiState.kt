package jaw.view.contract

import shared.model.ToothNumber

data class SelectionUiState(val selectedToothNumbers: MutableList<ToothNumber> = mutableListOf())