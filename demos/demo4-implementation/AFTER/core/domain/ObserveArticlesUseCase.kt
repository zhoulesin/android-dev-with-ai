package com.example.bestpractice.core.domain

import kotlinx.coroutines.flow.Flow

class ObserveArticlesUseCase(private val repository: ArticleRepository) {
    operator fun invoke(): Flow<List<Article>> = repository.observeArticles()
}
