package com.example.bestpractice.feature.feed

import com.example.bestpractice.core.domain.Article

data class FeedUiState(
    val articles: List<Article> = emptyList(),
    val isLoading: Boolean = true,
    val error: String? = null
)
