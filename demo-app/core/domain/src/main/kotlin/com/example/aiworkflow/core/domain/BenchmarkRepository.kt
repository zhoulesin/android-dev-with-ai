package com.example.aiworkflow.core.domain

import com.example.aiworkflow.core.common.Result
import com.example.aiworkflow.core.model.BenchmarkScenario
import com.example.aiworkflow.core.model.ScenarioDetail

interface BenchmarkRepository {
    fun listScenarios(): Result<List<BenchmarkScenario>>

    fun getScenarioDetail(id: String): Result<ScenarioDetail>
}
