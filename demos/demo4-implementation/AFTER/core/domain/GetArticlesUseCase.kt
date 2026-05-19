package com.example.bestpractice.core.domain

class GetArticlesUseCase(private val repository: ArticleRepository) {
    suspend operator fun invoke(): List<Article> = repository.getArticles()
}
