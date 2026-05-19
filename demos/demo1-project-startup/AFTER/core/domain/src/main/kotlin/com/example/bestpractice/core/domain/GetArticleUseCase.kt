package com.example.bestpractice.core.domain

class GetArticleUseCase(private val repository: ArticleRepository) {
    suspend operator fun invoke(id: String): Article? = repository.getArticle(id)
}
