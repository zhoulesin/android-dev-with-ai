package com.example.aiworkflow.core.data

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.preferencesDataStore
import com.example.aiworkflow.core.domain.ChecklistRepository
import com.example.aiworkflow.core.model.ChecklistItem
import com.example.aiworkflow.core.model.ChecklistSection
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

private val Context.checklistDataStore: DataStore<Preferences> by preferencesDataStore(name = "release_checklist")

@Singleton
class ReleaseChecklistRepository @Inject constructor(
    @ApplicationContext private val context: Context
) : ChecklistRepository {

    private val sections = listOf(
        ChecklistSection(
            sectionTitle = "Gradle / 依赖",
            items = listOf(
                ChecklistItem("gradle-catalog", "版本号仅在 gradle/libs.versions.toml"),
                ChecklistItem("gradle-api-impl", "暴露类型用 api，内部实现用 implementation"),
                ChecklistItem("gradle-compile", "./gradlew :app:assembleRelease 已通过"),
                ChecklistItem("gradle-deps", "依赖有变更时已查传递依赖，无 Duplicate class")
            )
        ),
        ChecklistSection(
            sectionTitle = "Manifest",
            items = listOf(
                ChecklistItem("manifest-merged", "已查看 Merged Manifest（Release）"),
                ChecklistItem("manifest-debug", "Debug 配置未泄漏到 Release"),
                ChecklistItem("manifest-sdk", "未为合并冲突删除第三方 SDK 的组件")
            )
        ),
        ChecklistSection(
            sectionTitle = "AI 参与变更",
            items = listOf(
                ChecklistItem("ai-gradle-diff", "build.gradle.kts / libs.versions.toml 已人工 Diff"),
                ChecklistItem("ai-sealed-when", "新增 sealed 子类对应 when 分支已穷尽"),
                ChecklistItem("ai-secrets", "提交中无 local.properties、google-services.json、密钥")
            )
        ),
        ChecklistSection(
            sectionTitle = "灰度与回滚",
            items = listOf(
                ChecklistItem("release-ratio", "灰度比例与观察窗口已确认"),
                ChecklistItem("release-rollback", "回滚条件与负责人已写明")
            )
        )
    )

    override fun observeChecklist(): Flow<List<ChecklistSection>> {
        return context.checklistDataStore.data.map { prefs ->
            sections.map { section ->
                section.copy(
                    items = section.items.map { item ->
                        item.copy(checked = prefs[booleanPreferencesKey(item.id)] ?: false)
                    }
                )
            }
        }
    }

    override suspend fun toggleItem(itemId: String) {
        context.checklistDataStore.edit { prefs ->
            val key = booleanPreferencesKey(itemId)
            prefs[key] = !(prefs[key] ?: false)
        }
    }

    override suspend fun resetAll() {
        context.checklistDataStore.edit { it.clear() }
    }

    override fun generatePrDescription(): String {
        val sb = StringBuilder()
        sb.appendLine("## 发包前检查（Release）")
        sb.appendLine()
        for (section in sections) {
            sb.appendLine("### ${section.sectionTitle}")
            sb.appendLine()
            for (item in section.items) {
                val checked = if (item.checked) "x" else " "
                sb.appendLine("- [$checked] ${item.text}")
            }
            sb.appendLine()
        }
        val checked = sections.sumOf { s -> s.items.count { it.checked } }
        val total = sections.sumOf { s -> s.items.size }
        sb.appendLine("> 完成: $checked / $total")
        return sb.toString()
    }
}
