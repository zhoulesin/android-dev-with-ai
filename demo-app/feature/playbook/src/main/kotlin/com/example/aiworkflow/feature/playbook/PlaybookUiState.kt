package com.example.aiworkflow.feature.playbook

import com.example.aiworkflow.core.model.ContentDocument
import com.example.aiworkflow.core.model.PlaybookTab
import com.example.aiworkflow.core.common.Result

data class PlaybookUiState(
    val tab: PlaybookTab = PlaybookTab.Rules,
    val documents: Result<List<ContentDocument>> = Result.Success(emptyList()),
    val selectedDocument: ContentDocument? = null
)
