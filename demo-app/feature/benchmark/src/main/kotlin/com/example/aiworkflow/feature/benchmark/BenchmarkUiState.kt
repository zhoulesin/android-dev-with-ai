package com.example.aiworkflow.feature.benchmark

import com.example.aiworkflow.core.common.Result
import com.example.aiworkflow.core.model.BenchmarkScenario
import com.example.aiworkflow.core.model.ScenarioDetail

data class BenchmarkUiState(
    val mode: BenchmarkMode = BenchmarkMode.List,
    val scenarios: Result<List<BenchmarkScenario>> = Result.Success(emptyList()),
    val detail: Result<ScenarioDetail>? = null
)

enum class BenchmarkMode { List, Detail }
