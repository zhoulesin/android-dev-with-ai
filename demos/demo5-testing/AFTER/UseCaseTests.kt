package com.example.bestpractice.core.domain

import app.cash.turbine.test
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class GetArticlesUseCaseTest {
    private val repo = FakeArticleRepository()
    private val useCase = GetArticlesUseCase(repo)

    @Test
    fun `returns all articles`() = runTest {
        val articles = useCase()
        assertEquals(2, articles.size)
    }
}

class ToggleBookmarkUseCaseTest {
    private val repo = FakeArticleRepository()
    private val useCase = ToggleBookmarkUseCase(repo)

    @Test
    fun `toggle unchecked to checked`() = runTest {
        useCase("1")
        val article = repo.getArticle("1")
        assertTrue(article?.isBookmarked == true)
    }

    @Test
    fun `toggle checked to unchecked`() = runTest {
        useCase("2")
        val article = repo.getArticle("2")
        assertTrue(article?.isBookmarked == false)
    }
}

class SearchArticlesUseCaseTest {
    private val repo = FakeArticleRepository()
    private val useCase = SearchArticlesUseCase(repo)

    @Test
    fun `search finds matching articles`() = runTest {
        useCase("Kotlin").test {
            val results = awaitItem()
            assertEquals(1, results.size)
            assertEquals("Title 1", results[0].title)
        }
    }
}
