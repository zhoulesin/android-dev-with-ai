package com.example.bestpractice.feature.search

import com.example.bestpractice.core.domain.Article

data class SearchUiState(
    val query: String = "",
    val results: List<Article> = emptyList()
)
