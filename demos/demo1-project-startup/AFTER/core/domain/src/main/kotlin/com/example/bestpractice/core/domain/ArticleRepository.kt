package com.example.bestpractice.core.domain

import kotlinx.coroutines.flow.Flow

interface ArticleRepository {
    suspend fun getArticles(): List<Article>
    suspend fun getArticle(id: String): Article?
    fun searchArticles(query: String): Flow<List<Article>>
    suspend fun toggleBookmark(id: String)
    fun observeArticles(): Flow<List<Article>>
    fun observeBookmarks(): Flow<List<Article>>
}
