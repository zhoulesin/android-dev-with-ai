package com.example.aiworkflow.core.domain

import com.example.aiworkflow.core.common.Result
import com.example.aiworkflow.core.model.ContentDocument
import com.example.aiworkflow.core.model.PlaybookTab

class ListPlaybookDocumentsUseCase(
    private val contentRepository: ContentRepository,
) {
    operator fun invoke(tab: PlaybookTab): Result<List<ContentDocument>> {
        return contentRepository.listDocuments(
            when (tab) {
                PlaybookTab.Rules -> "rules"
                PlaybookTab.Skills -> "skills"
                PlaybookTab.Prompts -> "prompts"
            },
        )
    }
}
