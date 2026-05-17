package com.example.aiworkflow.feature.release

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.aiworkflow.core.domain.ChecklistRepository
import com.example.aiworkflow.core.domain.ObserveReleaseChecklistUseCase
import com.example.aiworkflow.core.domain.ResetReleaseChecklistUseCase
import com.example.aiworkflow.core.domain.ToggleChecklistItemUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ReleaseViewModel @Inject constructor(
    private val observeReleaseChecklistUseCase: ObserveReleaseChecklistUseCase,
    private val toggleChecklistItemUseCase: ToggleChecklistItemUseCase,
    private val resetReleaseChecklistUseCase: ResetReleaseChecklistUseCase,
    private val checklistRepository: ChecklistRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(ReleaseUiState())
    val uiState: StateFlow<ReleaseUiState> = _uiState.asStateFlow()

    init {
        viewModelScope.launch {
            observeReleaseChecklistUseCase().collect { sections ->
                _uiState.update { it.copy(sections = sections) }
            }
        }
    }

    fun toggleItem(itemId: String) {
        viewModelScope.launch {
            toggleChecklistItemUseCase(itemId)
        }
    }

    fun resetAll() {
        viewModelScope.launch {
            resetReleaseChecklistUseCase()
        }
    }

    fun generatePrDescription() {
        val desc = checklistRepository.generatePrDescription()
        _uiState.update { it.copy(prDescription = desc, showPrDescription = true) }
    }

    fun hidePrDescription() {
        _uiState.update { it.copy(showPrDescription = false) }
    }
}
