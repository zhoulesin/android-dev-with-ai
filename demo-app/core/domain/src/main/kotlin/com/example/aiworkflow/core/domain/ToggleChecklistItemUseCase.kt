package com.example.aiworkflow.core.domain

class ToggleChecklistItemUseCase(
    private val checklistRepository: ChecklistRepository,
) {
    suspend operator fun invoke(itemId: String) {
        checklistRepository.toggleItem(itemId)
    }
}
