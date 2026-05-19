package com.example.bestpractice.feature.bookmark

import com.example.bestpractice.core.domain.Article

data class BookmarkUiState(
    val articles: List<Article> = emptyList(),
    val isLoading: Boolean = true
)
