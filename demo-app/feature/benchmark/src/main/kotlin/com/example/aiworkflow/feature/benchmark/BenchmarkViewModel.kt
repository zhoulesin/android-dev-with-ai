package com.example.aiworkflow.feature.benchmark

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.aiworkflow.core.domain.GetBenchmarkDetailUseCase
import com.example.aiworkflow.core.domain.ListBenchmarkScenariosUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class BenchmarkViewModel @Inject constructor(
    private val listBenchmarkScenariosUseCase: ListBenchmarkScenariosUseCase,
    private val getBenchmarkDetailUseCase: GetBenchmarkDetailUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(BenchmarkUiState())
    val uiState: StateFlow<BenchmarkUiState> = _uiState.asStateFlow()

    init {
        loadScenarios()
    }

    fun selectScenario(scenarioId: String) {
        viewModelScope.launch {
            _uiState.update { it.copy(mode = BenchmarkMode.Detail, detail = null) }
            val result = getBenchmarkDetailUseCase(scenarioId)
            _uiState.update { it.copy(detail = result) }
        }
    }

    fun backToList() {
        _uiState.update { it.copy(mode = BenchmarkMode.List, detail = null) }
    }

    private fun loadScenarios() {
        viewModelScope.launch {
            val result = listBenchmarkScenariosUseCase()
            _uiState.update { it.copy(scenarios = result) }
        }
    }
}
