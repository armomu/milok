package com.milok.app.screen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import dagger.hilt.android.EntryPointAccessors
import com.milok.app.data.model.Station
import com.milok.app.components.ErrorView
import com.milok.app.di.HomeViewModelEntryPoint
import com.milok.app.theme.MilokTheme
import com.milok.app.viewmodel.AppViewModel
import com.milok.app.viewmodel.HomeViewModel
import kotlinx.coroutines.flow.distinctUntilChanged

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    appViewModel: AppViewModel,
    onNavigateToDetail: (Int) -> Unit,
    modifier: Modifier = Modifier,
    onNavigateToScan: () -> Unit
) {
    // 通过 Hilt EntryPoint 获取 Repository，再用 ViewModelProvider.Factory 创建 HomeViewModel
    val context = LocalContext.current
    val entryPoint = remember {
        EntryPointAccessors.fromApplication(
            context.applicationContext,
            HomeViewModelEntryPoint::class.java
        )
    }
    val homeViewModel: HomeViewModel = viewModel(
        factory = HomeViewModel.factory(entryPoint.stationRepository())
    )

    val homeState = homeViewModel.uiState
    var searchText by remember { mutableStateOf(homeState.searchName) }

    // 列表滚动状态，用于检测触底上拉加载更多
    val listState = rememberLazyListState()

    // 监听列表滚动到底部，触发加载更多
    LaunchedEffect(listState) {
        snapshotFlow {
            val layoutInfo = listState.layoutInfo
            val totalItems = layoutInfo.totalItemsCount
            val lastVisibleIndex = (layoutInfo.visibleItemsInfo.lastOrNull()?.index ?: 0)
            lastVisibleIndex >= totalItems - 3
        }
            .distinctUntilChanged()
            .collect { nearBottom ->
                if (nearBottom && homeState.hasMore && !homeState.isLoadingMore) {
                    homeViewModel.loadMore()
                }
            }
    }
    Box(modifier = modifier) {
        when {
            homeState.isLoading && homeState.stations.isEmpty() -> {
                // 首次加载中
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator()
                }
            }

            homeState.errorMessage != null && homeState.stations.isEmpty() -> {
                // 错误且无数据
                ErrorView(
                    message = homeState.errorMessage,
                    onRetry = { homeViewModel.retry() }
                )
            }

            else -> {
                // 正常列表 + 下拉刷新
                PullToRefreshBox(
                    isRefreshing = homeState.isRefreshing,
                    onRefresh = { homeViewModel.refresh() },
                    modifier = Modifier.fillMaxSize()
                ) {
                    LazyColumn(
                        state = listState,
                        contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp),
                        verticalArrangement = Arrangement.spacedBy(8.dp),
                        modifier = Modifier.fillMaxSize()
                    ) {
                        items(homeState.stations, key = { it.id }) { station ->
                            StationCard(
                                station = station,
                                onClick = { onNavigateToDetail(station.id) }
                            )
                        }

                        // 底部加载更多指示器
                        if (homeState.isLoadingMore) {
                            item {
                                Box(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(vertical = 12.dp),
                                    contentAlignment = Alignment.Center
                                ) {
                                    CircularProgressIndicator(modifier = Modifier.size(24.dp))
                                }
                            }
                        }

                        // 已加载全部提示
                        if (!homeState.hasMore && homeState.stations.isNotEmpty()) {
                            item {
                                Box(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(vertical = 12.dp),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Text(
                                        text = "已加载全部 ${homeState.totalCount} 条数据",
                                        style = MaterialTheme.typography.bodySmall,
                                        color = MaterialTheme.colorScheme.onSurfaceVariant
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun StationCard(
    station: Station,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        onClick = onClick,
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant
        )
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                text = station.name,
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
            Spacer(modifier = Modifier.height(8.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = "编号: ${station.serialNumber}",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                Text(
                    text = "状态: ${if (station.status == 0) "正常" else "异常"}",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = "运营商: ${station.operator}",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            Text(
                text = "联系电话: ${station.operatorPhone}",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}

// ─── Preview ─────────────────────────────────────────────────────
@Preview(name = "StationCard - Light", showBackground = true)
@Composable
private fun StationCardLightPreview() {
    MilokTheme(darkTheme = false) {
        Column(modifier = Modifier.padding(16.dp)) {
            StationCard(
                station = Station(
                    id = 1,
                    name = "测试场站",
                    serialNumber = "CS00000000001",
                    operator = "张三",
                    operatorPhone = "13557112800",
                    status = 0,
                    createTime = "2026-03-19T17:33:47.286Z",
                    updateTime = "2026-03-19T17:33:47.286Z"
                ),
                onClick = {}
            )
        }
    }
}

@Preview(name = "StationCard - Dark", showBackground = true, backgroundColor = 0xFF1C1B1F)
@Composable
private fun StationCardDarkPreview() {
    MilokTheme(darkTheme = true) {
        Column(modifier = Modifier.padding(16.dp)) {
            StationCard(
                station = Station(
                    id = 2,
                    name = "深色模式示例场站",
                    serialNumber = "CS00000000002",
                    operator = "李四",
                    operatorPhone = "13800138000",
                    status = 1,
                    createTime = "2026-03-20T10:00:00.000Z",
                    updateTime = "2026-03-20T10:00:00.000Z"
                ),
                onClick = {}
            )
        }
    }
}
