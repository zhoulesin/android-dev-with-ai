package com.example.aiworkflow.core.model

data class BenchmarkScenario(
    val id: String,
    val title: String,
    val fixturePath: String,
    val summary: String,
    val scenarioLabel: String
)
