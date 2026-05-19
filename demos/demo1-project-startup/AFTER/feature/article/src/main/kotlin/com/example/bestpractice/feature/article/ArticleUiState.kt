package com.example.bestpractice.feature.article

import com.example.bestpractice.core.domain.Article

data class ArticleUiState(
    val article: Article? = null,
    val isLoading: Boolean = true,
    val error: String? = null
)
