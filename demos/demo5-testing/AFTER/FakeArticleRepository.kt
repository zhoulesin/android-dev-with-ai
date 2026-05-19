package com.example.bestpractice.core.domain

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.map

class FakeArticleRepository : ArticleRepository {
    private val articles = MutableStateFlow(
        listOf(
            Article("1", "Title 1", "Summary 1", "Content 1", "Author 1", "2025-01-01", listOf("Kotlin")),
            Article("2", "Title 2", "Summary 2", "Content 2", "Author 2", "2025-01-02", listOf("Compose"), isBookmarked = true)
        )
    )

    override suspend fun getArticles(): List<Article> = articles.value

    override suspend fun getArticle(id: String): Article? = articles.value.find { it.id == id }

    override fun searchArticles(query: String): Flow<List<Article>> =
        articles.map { list -> list.filter { it.title.contains(query, ignoreCase = true) } }

    override suspend fun toggleBookmark(id: String) {
        articles.value = articles.value.map {
            if (it.id == id) it.copy(isBookmarked = !it.isBookmarked) else it
        }
    }

    override fun observeArticles(): Flow<List<Article>> = articles

    override fun observeBookmarks(): Flow<List<Article>> =
        articles.map { list -> list.filter { it.isBookmarked } }
}
