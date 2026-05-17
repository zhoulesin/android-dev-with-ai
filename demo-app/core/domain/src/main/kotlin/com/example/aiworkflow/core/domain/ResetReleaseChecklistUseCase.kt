package com.example.aiworkflow.core.domain

class ResetReleaseChecklistUseCase(
    private val checklistRepository: ChecklistRepository,
) {
    suspend operator fun invoke() {
        checklistRepository.resetAll()
    }
}
