package com.example.aiworkflow.core.domain

import com.example.aiworkflow.core.common.Result
import com.example.aiworkflow.core.model.ChecklistSection
import kotlinx.coroutines.flow.first

class GetReleaseChecklistUseCase(
    private val observeReleaseChecklistUseCase: ObserveReleaseChecklistUseCase,
) {
    suspend operator fun invoke(): Result<List<ChecklistSection>> =
        try {
            val sections = observeReleaseChecklistUseCase().first()
            Result.Success(sections)
        } catch (e: Exception) {
            Result.Error("Failed to load checklist: ${e.message}", e)
        }
}
