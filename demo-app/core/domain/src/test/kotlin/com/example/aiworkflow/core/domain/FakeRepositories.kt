package com.example.aiworkflow.core.domain

import com.example.aiworkflow.core.common.Result
import com.example.aiworkflow.core.model.BenchmarkScenario
import com.example.aiworkflow.core.model.ChecklistItem
import com.example.aiworkflow.core.model.ChecklistSection
import com.example.aiworkflow.core.model.ScenarioDetail
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update

class FakeBenchmarkRepository : BenchmarkRepository {
    private val scenarios =
        listOf(
            BenchmarkScenario(
                id = "s01",
                title = "考卷A：dup class",
                fixturePath = "fixtures/01",
                summary = "summary1",
                scenarioLabel = "考卷A",
            ),
            BenchmarkScenario(
                id = "s02",
                title = "考卷B：api vs impl",
                fixturePath = "fixtures/02",
                summary = "summary2",
                scenarioLabel = "考卷B",
            ),
        )

    override fun listScenarios(): Result<List<BenchmarkScenario>> = Result.Success(scenarios)

    override fun getScenarioDetail(id: String): Result<ScenarioDetail> {
        val s = scenarios.firstOrNull { it.id == id }
        return if (s != null) {
            Result.Success(ScenarioDetail(s, "readme content", "prompt content"))
        } else {
            Result.Error("Not found: $id")
        }
    }
}

class FakeChecklistRepository : ChecklistRepository {
    private val sectionsFlow =
        MutableStateFlow(
            listOf(
                ChecklistSection(
                    sectionTitle = "Section A",
                    items =
                        listOf(
                            ChecklistItem(id = "a1", text = "Item A1"),
                            ChecklistItem(id = "a2", text = "Item A2", checked = true),
                        ),
                ),
            ),
        )

    override fun observeChecklist(): Flow<List<ChecklistSection>> = sectionsFlow

    override suspend fun toggleItem(itemId: String) {
        sectionsFlow.update { sections ->
            sections.map { section ->
                section.copy(
                    items =
                        section.items.map { item ->
                            if (item.id == itemId) {
                                item.copy(checked = !item.checked)
                            } else {
                                item
                            }
                        },
                )
            }
        }
    }

    override suspend fun resetAll() {
        sectionsFlow.update { sections ->
            sections.map { section ->
                section.copy(
                    items =
                        section.items.map { it.copy(checked = false) },
                )
            }
        }
    }

    override fun generatePrDescription(): String = "checklist pr description"
}
