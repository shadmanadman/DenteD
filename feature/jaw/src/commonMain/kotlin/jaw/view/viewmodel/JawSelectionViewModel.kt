package jaw.view.viewmodel

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import shared.model.ToothNumber

class JawSelectionViewModel : ViewModel() {
    private val _selectedTeeth = MutableStateFlow<List<ToothNumber>>(emptyList())
    val selectedTeeth: StateFlow<List<ToothNumber>> = _selectedTeeth

    fun addSelectedTooth(toothNumber: ToothNumber) {
        _selectedTeeth.value = _selectedTeeth.value.toMutableList().apply {
            add(toothNumber)
        }
    }

    fun removeSelectedTooth(toothNumber: ToothNumber) {
        _selectedTeeth.value = _selectedTeeth.value.toMutableList().apply {
            remove(toothNumber)
        }
    }
}