package com.example.aiworkflow.core.domain

import com.example.aiworkflow.core.common.Result
import com.example.aiworkflow.core.model.ContentDocument

interface ContentRepository {
    fun getDocument(id: String): Result<ContentDocument>

    fun listDocuments(category: String): Result<List<ContentDocument>>
}
