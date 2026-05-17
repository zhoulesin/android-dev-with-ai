package com.example.aiworkflow.core.model

data class ContentDocument(
    val id: String,
    val title: String,
    val path: String,
    val category: String,
    val bodyMarkdown: String
)
