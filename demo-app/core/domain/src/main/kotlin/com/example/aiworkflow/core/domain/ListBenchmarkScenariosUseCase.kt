package com.example.aiworkflow.core.domain

import com.example.aiworkflow.core.common.Result
import com.example.aiworkflow.core.model.BenchmarkScenario

class ListBenchmarkScenariosUseCase(
    private val benchmarkRepository: BenchmarkRepository,
) {
    operator fun invoke(): Result<List<BenchmarkScenario>> {
        return benchmarkRepository.listScenarios()
    }
}
