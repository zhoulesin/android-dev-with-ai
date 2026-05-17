package com.example.aiworkflow.core.model

data class ChecklistSection(
    val sectionTitle: String,
    val items: List<ChecklistItem>
)

data class ChecklistItem(
    val id: String,
    val text: String,
    val checked: Boolean = false
)
