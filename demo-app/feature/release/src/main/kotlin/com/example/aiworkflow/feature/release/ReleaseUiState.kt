package com.example.aiworkflow.feature.release

import com.example.aiworkflow.core.model.ChecklistSection

data class ReleaseUiState(
    val sections: List<ChecklistSection> = emptyList(),
    val prDescription: String = "",
    val showPrDescription: Boolean = false
)
