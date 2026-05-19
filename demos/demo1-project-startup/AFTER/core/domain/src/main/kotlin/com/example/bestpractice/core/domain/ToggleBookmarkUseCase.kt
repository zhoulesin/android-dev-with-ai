package com.example.bestpractice.core.domain

class ToggleBookmarkUseCase(private val repository: ArticleRepository) {
    suspend operator fun invoke(id: String) = repository.toggleBookmark(id)
}
