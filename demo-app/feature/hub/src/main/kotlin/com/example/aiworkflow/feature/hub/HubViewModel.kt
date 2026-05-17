package com.example.aiworkflow.feature.hub

import androidx.lifecycle.ViewModel
import com.example.aiworkflow.core.domain.GetHubDestinationsUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class HubViewModel @Inject constructor(
    getHubDestinationsUseCase: GetHubDestinationsUseCase
) : ViewModel() {

    val uiState = HubUiState(
        destinations = getHubDestinationsUseCase()
    )
}
