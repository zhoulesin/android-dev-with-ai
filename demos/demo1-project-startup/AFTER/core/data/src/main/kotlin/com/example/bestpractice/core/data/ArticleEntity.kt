package com.example.bestpractice.core.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "articles")
data class ArticleEntity(
    @PrimaryKey val id: String,
    val title: String,
    val summary: String,
    val content: String,
    val author: String,
    val publishDate: String,
    val tags: String,
    val isBookmarked: Boolean = false
)
