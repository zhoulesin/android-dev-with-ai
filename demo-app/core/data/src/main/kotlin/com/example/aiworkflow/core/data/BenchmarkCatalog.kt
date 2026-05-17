package com.example.aiworkflow.core.data

import android.content.res.AssetManager
import com.example.aiworkflow.core.common.Result
import com.example.aiworkflow.core.model.BenchmarkScenario
import com.example.aiworkflow.core.model.ScenarioDetail
import com.example.aiworkflow.core.domain.BenchmarkRepository
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class BenchmarkCatalog @Inject constructor(
    private val assetManager: AssetManager
) : BenchmarkRepository {

    private val scenarios = listOf(
        scenario("scenario-01-gradle-duplicate-class", "考卷A", "Room 传递依赖版本冲突，exclude / force / catalog"),
        scenario("scenario-02-api-vs-implementation", "考卷B", "模块暴露类型与 api/implementation 声明"),
        scenario("scenario-03-version-catalog", "考卷C", "只改 libs.versions.toml 引发连锁"),
        scenario("scenario-04-sealed-when", "考卷D", "sealed 事件 + when 穷尽"),
        scenario("scenario-05-compose-chat-list", "考卷E", "Compose 聊天列表（需求 + 验收，无预置代码）"),
        scenario("scenario-07-manifest-merge", "考卷07", "Manifest 合并冲突（debug exported / SDK 节点）"),
        scenario("scenario-08-layered-modules", "考卷08", "app/feature/domain/data 引用溯源")
    )

    override fun listScenarios(): Result<List<BenchmarkScenario>> = try {
        Result.Success(scenarios)
    } catch (e: Exception) {
        Result.Error("Failed to list scenarios", e)
    }

    override fun getScenarioDetail(id: String): Result<ScenarioDetail> = try {
        val scenario = scenarios.firstOrNull { it.id == id }
            ?: return Result.Error("Scenario not found: $id")
        val basePath = "fixtures/android-benchmark/$id"
        val readme = assetManager.open("$basePath/README.md").bufferedReader().use { it.readText() }
        val prompt = readAssetOrEmpty("$basePath/PROMPT.md")
        Result.Success(ScenarioDetail(scenario, readme, prompt))
    } catch (e: Exception) {
        Result.Error("Failed to read scenario detail: ${e.message}", e)
    }

    private fun readAssetOrEmpty(path: String): String = try {
        assetManager.open(path).bufferedReader().use { it.readText() }
    } catch (_: Exception) {
        ""
    }

    private fun scenario(id: String, label: String, summary: String) = BenchmarkScenario(
        id = id,
        title = "$label：$summary",
        fixturePath = "fixtures/android-benchmark/$id",
        summary = summary,
        scenarioLabel = label
    )
}
