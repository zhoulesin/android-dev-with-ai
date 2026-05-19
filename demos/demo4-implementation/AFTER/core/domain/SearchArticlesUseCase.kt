package com.example.bestpractice.core.domain

class SearchArticlesUseCase(private val repository: ArticleRepository) {
    operator fun invoke(query: String) = repository.searchArticles(query)
}
