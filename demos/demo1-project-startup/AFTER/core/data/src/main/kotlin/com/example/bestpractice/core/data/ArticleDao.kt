package com.example.bestpractice.core.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface ArticleDao {
    @Query("SELECT * FROM articles ORDER BY publishDate DESC")
    fun observeAll(): Flow<List<ArticleEntity>>

    @Query("SELECT * FROM articles WHERE id = :id")
    suspend fun getById(id: String): ArticleEntity?

    @Query("SELECT * FROM articles WHERE isBookmarked = 1 ORDER BY publishDate DESC")
    fun observeBookmarks(): Flow<List<ArticleEntity>>

    @Query("SELECT * FROM articles WHERE title LIKE '%' || :query || '%' OR summary LIKE '%' || :query || '%'")
    fun search(query: String): Flow<List<ArticleEntity>>

    @Query("UPDATE articles SET isBookmarked = :bookmarked WHERE id = :id")
    suspend fun setBookmarked(id: String, bookmarked: Boolean)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(articles: List<ArticleEntity>)

    @Query("SELECT COUNT(*) FROM articles")
    suspend fun count(): Int
}
