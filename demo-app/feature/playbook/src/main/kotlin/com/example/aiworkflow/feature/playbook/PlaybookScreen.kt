package com.example.aiworkflow.feature.playbook

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
import androidx.compose.material3.FilterChip
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
import com.example.aiworkflow.core.model.PlaybookTab

@Composable
fun PlaybookScreen(
    onBack: () -> Unit,
    viewModel: PlaybookViewModel = hiltViewModel(),
    modifier: Modifier = Modifier
) {
    val state by viewModel.uiState.collectAsStateWithLifecycle()
    val doc = state.selectedDocument

    if (doc != null) {
        DocumentDetail(
            title = doc.title,
            markdown = doc.bodyMarkdown,
            onBack = { viewModel.clearSelection() },
            modifier = modifier
        )
        return
    }

    AppScaffold(
        title = "配置",
        onBack = onBack,
        modifier = modifier
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
        ) {
            PlaybookTabRow(
                selectedTab = state.tab,
                onTabSelected = { viewModel.selectTab(it) }
            )
            HorizontalDivider()
            when (val docs = state.documents) {
                is Result.Success -> {
                    if (docs.data.isEmpty()) {
                        EmptyState("暂无文档，请执行 sync-content.sh")
                    } else {
                        LazyColumn(
                            contentPadding = PaddingValues(16.dp),
                            verticalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            items(docs.data, key = { it.id }) { doc ->
                                Text(
                                    text = doc.title,
                                    style = MaterialTheme.typography.titleMedium,
                                    fontWeight = FontWeight.Medium,
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .clickable { viewModel.selectDocument(doc.id) }
                                        .padding(vertical = 12.dp)
                                )
                                HorizontalDivider()
                            }
                        }
                    }
                }
                is Result.Error -> ErrorState(message = docs.message)
            }
        }
    }
}

@Composable
fun PlaybookTabRow(
    selectedTab: PlaybookTab,
    onTabSelected: (PlaybookTab) -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        PlaybookTab.entries.forEach { tab ->
            FilterChip(
                selected = selectedTab == tab,
                onClick = { onTabSelected(tab) },
                label = { Text(tab.label) }
            )
        }
    }
}

@Composable
fun DocumentDetail(
    title: String,
    markdown: String,
    onBack: () -> Unit,
    modifier: Modifier = Modifier
) {
    AppScaffold(
        title = title,
        onBack = onBack,
        actions = { CopyButton(text = markdown) },
        modifier = modifier
    ) { padding ->
        MarkdownBody(
            markdown = markdown,
            modifier = Modifier.padding(padding)
        )
    }
}
