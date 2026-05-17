package com.example.aiworkflow.core.model

data class ScenarioDetail(
    val scenario: BenchmarkScenario,
    val readmeMarkdown: String,
    val promptMarkdown: String
)
