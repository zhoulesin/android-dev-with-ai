package com.example.bestpractice.core.domain

data class Article(
    val id: String,
    val title: String,
    val summary: String,
    val content: String,
    val author: String,
    val publishDate: String,
    val tags: List<String>,
    val isBookmarked: Boolean = false
)
