package com.example.feature.feed

import androidx.compose.foundation.layout.Box
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.pullrefresh.PullRefreshIndicator
import androidx.compose.material3.pullrefresh.pullRefresh
import androidx.compose.material3.pullrefresh.rememberPullRefreshState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

/**
 * 考题：BOM 2025.02 后 pullrefresh 包路径/API 可能变更，需模型给出迁移方案。
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FeedScreen(isRefreshing: Boolean, onRefresh: () -> Unit, modifier: Modifier = Modifier) {
    val state = rememberPullRefreshState(isRefreshing, onRefresh)
    Box(modifier.pullRefresh(state)) {
        PullRefreshIndicator(isRefreshing, state)
    }
}
