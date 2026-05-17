package com.example.aiworkflow.core.domain

import com.example.aiworkflow.core.model.ChecklistSection
import kotlinx.coroutines.flow.Flow

class ObserveReleaseChecklistUseCase(
    private val checklistRepository: ChecklistRepository,
) {
    operator fun invoke(): Flow<List<ChecklistSection>> {
        return checklistRepository.observeChecklist()
    }
}
