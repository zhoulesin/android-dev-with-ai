package com.example.aiworkflow.feature.playbook

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.aiworkflow.core.common.Result
import com.example.aiworkflow.core.domain.GetPlaybookDocumentUseCase
import com.example.aiworkflow.core.domain.ListPlaybookDocumentsUseCase
import com.example.aiworkflow.core.model.ContentDocument
import com.example.aiworkflow.core.model.PlaybookTab
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PlaybookViewModel @Inject constructor(
    private val listPlaybookDocumentsUseCase: ListPlaybookDocumentsUseCase,
    private val getPlaybookDocumentUseCase: GetPlaybookDocumentUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(PlaybookUiState())
    val uiState: StateFlow<PlaybookUiState> = _uiState.asStateFlow()

    private val skillsDescription = ContentDocument(
        id = "skills-intro",
        title = "Skills 与 MCP",
        path = "",
        category = "skills",
        bodyMarkdown = """
# Skills 是什么？

Skills 是 AI Coding 工具的能力增强模块，相当于给 AI 安装了领域专用「技能包」。

## 核心概念

- **Skills**：针对特定任务的工作流指令（如 Compose UI Review、Gradle 问题排查）
- **MCP**：Model Context Protocol，让 AI 能调用外部工具和 API
- **Rules**：项目宪法，定义项目结构和禁止项

## 如何使用

1. 在 Claude Code 中，Skills 通过 `.claude/skills/` 目录配置
2. 在 Cursor 中，通过 `.cursor/skills/` 或 Rules 目录配置
3. 将 [`templates/rules/CLAUDE.md`](https://github.com/cozlya/android-dev-with-ai/blob/main/templates/rules/CLAUDE.md) 复制到项目根目录

## 推荐 Skills

| Skill | 用途 |
|-------|------|
| brainstorming | 需求分析与方案设计 |
| android-compose-review | Compose UI 代码审查 |
| gradle-debug | Gradle 构建问题排查 |
| test-driven-development | TDD 开发流程 |

## 自定义 Skill

每个 Skill 是一个 `SKILL.md` 文件：

```markdown
# Skill: Compose Review
1. 检查 Modifier 顺序
2. 验证状态管理模式
3. 检查 recomposition 范围
```

> 详见文档：[第 4 章 · 效率神器：Skills 与 MCP](../../docs/04-效率神器Skills与MCP.md)
        """.trimIndent()
    )

    init {
        loadDocuments(PlaybookTab.Rules)
    }

    fun selectTab(tab: PlaybookTab) {
        _uiState.update { it.copy(tab = tab, selectedDocument = null) }
        loadDocuments(tab)
    }

    fun selectDocument(id: String) {
        if (id == "skills-intro") {
            _uiState.update { it.copy(selectedDocument = skillsDescription) }
            return
        }
        _uiState.update { state ->
            val docs = (state.documents as? Result.Success)?.data
            val doc = docs?.find { it.id == id }
            if (doc != null) {
                state.copy(selectedDocument = doc)
            } else {
                state
            }
        }
    }

    fun clearSelection() {
        _uiState.update { it.copy(selectedDocument = null) }
    }

    private fun loadDocuments(tab: PlaybookTab) {
        viewModelScope.launch {
            if (tab == PlaybookTab.Skills) {
                _uiState.update {
                    it.copy(documents = Result.Success(listOf(skillsDescription)))
                }
                return@launch
            }
            val result = listPlaybookDocumentsUseCase(tab)
            _uiState.update { it.copy(documents = result) }
        }
    }
}
