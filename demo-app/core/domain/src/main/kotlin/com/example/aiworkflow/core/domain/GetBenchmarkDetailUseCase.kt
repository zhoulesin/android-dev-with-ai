package com.example.aiworkflow.core.domain

import com.example.aiworkflow.core.common.Result
import com.example.aiworkflow.core.model.ScenarioDetail

class GetBenchmarkDetailUseCase(
    private val benchmarkRepository: BenchmarkRepository,
) {
    operator fun invoke(scenarioId: String): Result<ScenarioDetail> {
        return benchmarkRepository.getScenarioDetail(scenarioId)
    }
}
