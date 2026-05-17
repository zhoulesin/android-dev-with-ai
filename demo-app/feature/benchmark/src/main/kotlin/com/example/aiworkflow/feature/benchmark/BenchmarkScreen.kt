package com.example.aiworkflow.feature.benchmark

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.aiworkflow.core.common.Result
import com.example.aiworkflow.core.designsystem.AppScaffold
import com.example.aiworkflow.core.designsystem.CopyButton
import com.example.aiworkflow.core.designsystem.EmptyState
import com.example.aiworkflow.core.designsystem.ErrorState
import com.example.aiworkflow.core.designsystem.LoadingState
import com.example.aiworkflow.core.designsystem.MarkdownBody
import com.example.aiworkflow.core.model.ScenarioDetail

@Composable
fun BenchmarkScreen(
    onBack: () -> Unit,
    viewModel: BenchmarkViewModel = hiltViewModel(),
    modifier: Modifier = Modifier
) {
    val state by viewModel.uiState.collectAsStateWithLifecycle()

    when (state.mode) {
        BenchmarkMode.List -> BenchmarkList(
            state = state,
            onScenarioClick = { viewModel.selectScenario(it) },
            onBack = onBack,
            modifier = modifier
        )
        BenchmarkMode.Detail -> BenchmarkDetail(
            detail = state.detail,
            onBack = { viewModel.backToList() },
            modifier = modifier
        )
    }
}

@Composable
private fun BenchmarkList(
    state: BenchmarkUiState,
    onScenarioClick: (String) -> Unit,
    onBack: () -> Unit,
    modifier: Modifier = Modifier
) {
    AppScaffold(
        title = "考卷",
        onBack = onBack,
        modifier = modifier
    ) { padding ->
        when (val scenarios = state.scenarios) {
            is Result.Success -> {
                if (scenarios.data.isEmpty()) {
                    EmptyState("暂无考卷数据")
                } else {
                    LazyColumn(
                        modifier = Modifier.padding(padding),
                        contentPadding = PaddingValues(16.dp),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        items(scenarios.data, key = { it.id }) { scenario ->
                            Card(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clickable { onScenarioClick(scenario.id) }
                            ) {
                                Column(modifier = Modifier.padding(16.dp)) {
                                    Text(
                                        text = scenario.scenarioLabel,
                                        style = MaterialTheme.typography.labelLarge,
                                        color = MaterialTheme.colorScheme.primary
                                    )
                                    Text(
                                        text = scenario.summary,
                                        style = MaterialTheme.typography.bodyMedium,
                                        fontWeight = FontWeight.Medium,
                                        modifier = Modifier.padding(top = 4.dp)
                                    )
                                }
                            }
                        }
                    }
                }
            }
            is Result.Error -> ErrorState(message = scenarios.message)
        }
    }
}

@Composable
private fun BenchmarkDetail(
    detail: Result<ScenarioDetail>?,
    onBack: () -> Unit,
    modifier: Modifier = Modifier
) {
    when (detail) {
        null -> {
            AppScaffold(title = "加载中...", onBack = onBack, modifier = modifier) { padding ->
                LoadingState(Modifier.padding(padding))
            }
        }
        is Result.Success -> {
            val data = detail.data
            AppScaffold(
                title = data.scenario.scenarioLabel,
                onBack = onBack,
                actions = {
                    CopyButton(text = data.readmeMarkdown, label = "README 已复制")
                    CopyButton(text = data.promptMarkdown, label = "Prompt 已复制")
                },
                modifier = modifier
            ) { padding ->
                Column(Modifier.fillMaxSize().padding(padding)) {
                    MarkdownBody(
                        markdown = data.readmeMarkdown,
                        modifier = Modifier.weight(0.45f)
                    )
                    HorizontalDivider(Modifier.padding(horizontal = 16.dp))
                    Text(
                        text = "PROMPT",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(16.dp, 8.dp)
                    )
                    MarkdownBody(
                        markdown = data.promptMarkdown,
                        modifier = Modifier.weight(0.4f)
                    )
                    HorizontalDivider(Modifier.padding(horizontal = 16.dp))
                    Text(
                        text = "关联章节：${data.scenario.summary}",
                        style = MaterialTheme.typography.labelSmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        modifier = Modifier.padding(16.dp)
                    )
                }
            }
        }
        is Result.Error -> {
            AppScaffold(title = "错误", onBack = onBack, modifier = modifier) { padding ->
                ErrorState(detail.message, modifier = Modifier.padding(padding))
            }
        }
    }
}
