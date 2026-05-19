package com.example.bestpractice.core.data

import com.example.bestpractice.core.domain.Article
import com.example.bestpractice.core.domain.ArticleRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ArticleRepositoryImpl @Inject constructor(
    private val articleDao: ArticleDao,
    private val remoteDataSource: FakeRemoteDataSource
) : ArticleRepository {

    override suspend fun getArticles(): List<Article> {
        if (articleDao.count() == 0) {
            val articles = remoteDataSource.fetchArticles()
            articleDao.insertAll(articles.map { it.toEntity() })
        }
        return articleDao.observeAll().map { entities ->
            entities.map { it.toDomain() }
        }.let { flow ->
            var result: List<Article> = emptyList()
            flow.collect { result = it; return@collect }
            result
        }
    }

    override suspend fun getArticle(id: String): Article? {
        return articleDao.getById(id)?.toDomain()
    }

    override fun searchArticles(query: String): Flow<List<Article>> {
        return articleDao.search(query).map { entities ->
            entities.map { it.toDomain() }
        }
    }

    override suspend fun toggleBookmark(id: String) {
        val entity = articleDao.getById(id) ?: return
        articleDao.setBookmarked(id, !entity.isBookmarked)
    }

    override fun observeArticles(): Flow<List<Article>> {
        return articleDao.observeAll().map { entities ->
            entities.map { it.toDomain() }
        }
    }

    override fun observeBookmarks(): Flow<List<Article>> {
        return articleDao.observeBookmarks().map { entities ->
            entities.map { it.toDomain() }
        }
    }
}

private fun Article.toEntity() = ArticleEntity(
    id = id,
    title = title,
    summary = summary,
    content = content,
    author = author,
    publishDate = publishDate,
    tags = tags.joinToString(","),
    isBookmarked = isBookmarked
)

private fun ArticleEntity.toDomain() = Article(
    id = id,
    title = title,
    summary = summary,
    content = content,
    author = author,
    publishDate = publishDate,
    tags = tags.split(",").filter { it.isNotEmpty() },
    isBookmarked = isBookmarked
)
