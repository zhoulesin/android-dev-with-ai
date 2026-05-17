package com.example.aiworkflow.feature.hub

import com.example.aiworkflow.core.model.HubDestination

data class HubUiState(
    val destinations: List<HubDestination> = emptyList()
)
