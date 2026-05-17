package com.example.aiworkflow.core.domain

import com.example.aiworkflow.core.common.Result
import com.example.aiworkflow.core.model.ContentDocument

class GetPlaybookDocumentUseCase(
    private val contentRepository: ContentRepository,
) {
    operator fun invoke(id: String): Result<ContentDocument> {
        return contentRepository.getDocument(id)
    }
}
