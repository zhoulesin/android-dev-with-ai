package com.example.bestpractice.core.data

import android.content.res.AssetManager
import com.example.bestpractice.core.domain.Article
import kotlinx.coroutines.delay
import org.json.JSONArray
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class FakeRemoteDataSource @Inject constructor(
    private val assetManager: AssetManager
) {
    suspend fun fetchArticles(): List<Article> {
        delay(400)
        val text = assetManager.open("articles.json").bufferedReader().use { it.readText() }
        val array = JSONArray(text)
        return (0 until array.length()).map { i ->
            val obj = array.getJSONObject(i)
            val tagsArray = obj.optJSONArray("tags")
            val tags = if (tagsArray != null) {
                (0 until tagsArray.length()).map { tagsArray.getString(it) }
            } else emptyList()
            Article(
                id = obj.getString("id"),
                title = obj.getString("title"),
                summary = obj.getString("summary"),
                content = obj.getString("content"),
                author = obj.getString("author"),
                publishDate = obj.getString("publishDate"),
                tags = tags,
                isBookmarked = false
            )
        }
    }
}
