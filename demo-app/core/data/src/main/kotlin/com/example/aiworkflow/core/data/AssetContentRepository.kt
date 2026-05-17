package com.example.aiworkflow.core.data

import android.content.res.AssetManager
import com.example.aiworkflow.core.common.Result
import com.example.aiworkflow.core.model.ContentDocument
import com.example.aiworkflow.core.domain.ContentRepository
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AssetContentRepository @Inject constructor(
    private val assetManager: AssetManager
) : ContentRepository {

    override fun getDocument(id: String): Result<ContentDocument> = try {
        val path = idToPath(id)
        val body = assetManager.open(path).bufferedReader().use { it.readText() }
        val doc = ContentDocument(
            id = id,
            title = pathToTitle(path),
            path = path,
            category = inferCategory(path),
            bodyMarkdown = body
        )
        Result.Success(doc)
    } catch (e: Exception) {
        Result.Error("Document not found: $id", e)
    }

    override fun listDocuments(category: String): Result<List<ContentDocument>> = try {
        val prefix = when (category) {
            "rules" -> "templates/rules"
            "skills" -> "templates/skills"
            "prompts" -> "fixtures/android-benchmark"
            else -> category
        }
        val list = assetManager.list(prefix) ?: emptyArray()
        val results = mutableListOf<ContentDocument>()
        for (entry in list) {
            val path = "$prefix/$entry"
            if (isMarkdownFile(entry)) {
                val body = assetManager.open(path).bufferedReader().use { it.readText() }
                results.add(
                    ContentDocument(
                        id = buildId(category, entry.removeSuffix(".md")),
                        title = entry.removeSuffix(".md"),
                        path = path,
                        category = category,
                        bodyMarkdown = body
                    )
                )
            } else {
                results.addAll(listNestedMarkdown(path, category, entry))
            }
        }
        Result.Success(results)
    } catch (e: Exception) {
        Result.Error("Failed to list documents for $category", e)
    }

    private fun listNestedMarkdown(dirPath: String, category: String, dirName: String): List<ContentDocument> {
        val list = assetManager.list(dirPath) ?: return emptyList()
        return list.filter { isMarkdownFile(it) }.map { entry ->
            val path = "$dirPath/$entry"
            val body = assetManager.open(path).bufferedReader().use { it.readText() }
            val nameWithoutExt = entry.removeSuffix(".md")
            ContentDocument(
                id = buildId(category, "$dirName-$nameWithoutExt"),
                title = "$dirName / $nameWithoutExt",
                path = path,
                category = category,
                bodyMarkdown = body
            )
        }
    }

    private fun buildId(category: String, name: String): String = "$category-$name"

    private fun idToPath(id: String): String {
        if (!id.contains("-")) return id

        val categoryEnd = id.indexOf("-")
        val category = id.substring(0, categoryEnd)
        val rest = id.substring(categoryEnd + 1)

        val basePath = when (category) {
            "rules" -> "templates/rules"
            "skills" -> "templates/skills"
            "prompts" -> "fixtures/android-benchmark"
            else -> category
        }

        val known = mapOf(
            "rules-CLAUDE" to "templates/rules/CLAUDE.md",
        )
        known[id]?.let { return it }

        val lastDash = rest.lastIndexOf("-")
        return if (lastDash >= 0) {
            val dirPart = rest.substring(0, lastDash)
            val filePart = rest.substring(lastDash + 1)
            "$basePath/$dirPart/$filePart.md"
        } else {
            "$basePath/$rest.md"
        }
    }

    private fun pathToTitle(path: String): String {
        val name = path.substringAfterLast("/").removeSuffix(".md")
        return name.replace("-", " ").replaceFirstChar { it.uppercase() }
    }

    private fun inferCategory(path: String): String = when {
        path.startsWith("templates/rules") -> "rules"
        path.startsWith("templates/skills") -> "skills"
        path.startsWith("fixtures/") -> "prompts"
        else -> "other"
    }

    private fun isMarkdownFile(name: String): Boolean =
        name.endsWith(".md") && !name.startsWith(".")
}
