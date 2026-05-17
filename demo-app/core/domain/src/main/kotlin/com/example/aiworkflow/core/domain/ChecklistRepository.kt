package com.example.aiworkflow.core.domain

import com.example.aiworkflow.core.model.ChecklistSection
import kotlinx.coroutines.flow.Flow

interface ChecklistRepository {
    fun observeChecklist(): Flow<List<ChecklistSection>>

    suspend fun toggleItem(itemId: String)

    suspend fun resetAll()

    fun generatePrDescription(): String
}
