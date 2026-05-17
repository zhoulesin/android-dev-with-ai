package com.example.aiworkflow.feature.release

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.Checkbox
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.aiworkflow.core.designsystem.AppScaffold
import com.example.aiworkflow.core.designsystem.CopyButton
import com.example.aiworkflow.core.designsystem.MarkdownBody

@Composable
fun ReleaseScreen(
    onBack: () -> Unit,
    viewModel: ReleaseViewModel = hiltViewModel(),
    modifier: Modifier = Modifier
) {
    val state by viewModel.uiState.collectAsStateWithLifecycle()

    if (state.showPrDescription) {
        AppScaffold(
            title = "PR 描述",
            onBack = { viewModel.hidePrDescription() },
            actions = { CopyButton(text = state.prDescription, label = "PR 描述已复制") },
            modifier = modifier
        ) { padding ->
            MarkdownBody(
                markdown = state.prDescription,
                modifier = Modifier.padding(padding)
            )
        }
        return
    }

    val checked = state.sections.sumOf { s -> s.items.count { it.checked } }
    val total = state.sections.sumOf { s -> s.items.size }

    AppScaffold(
        title = "发布检查 ($checked/$total)",
        onBack = onBack,
        modifier = modifier
    ) { padding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            item {
                Text(
                    text = "勾选须人工确认，AI 仅可生成初稿",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.error,
                    modifier = Modifier.padding(bottom = 8.dp)
                )
            }

            state.sections.forEach { section ->
                item {
                    Text(
                        text = section.sectionTitle,
                        style = MaterialTheme.typography.titleSmall,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(top = 8.dp, bottom = 4.dp)
                    )
                }
                items(section.items, key = { it.id }) { item ->
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Checkbox(
                            checked = item.checked,
                            onCheckedChange = { viewModel.toggleItem(item.id) }
                        )
                        Text(
                            text = item.text,
                            style = MaterialTheme.typography.bodyMedium,
                            modifier = Modifier.weight(1f)
                        )
                    }
                }
                item { HorizontalDivider(Modifier.padding(vertical = 4.dp)) }
            }

            item {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Button(
                        onClick = { viewModel.generatePrDescription() },
                        modifier = Modifier.weight(1f)
                    ) {
                        Text("复制为 PR 描述")
                    }
                    OutlinedButton(
                        onClick = { viewModel.resetAll() },
                        modifier = Modifier.weight(1f)
                    ) {
                        Text("全部重置")
                    }
                }
            }
        }
    }
}
