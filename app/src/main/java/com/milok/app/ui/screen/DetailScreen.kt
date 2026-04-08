package com.milok.app.ui.screen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.milok.app.data.model.Post
import com.milok.app.ui.components.ErrorView
import com.milok.app.ui.theme.MilokTheme
import com.milok.app.ui.viewmodel.AppViewModel
import com.milok.app.ui.viewmodel.DetailViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DetailScreen(
    postId: Int,
    appViewModel: AppViewModel,
    onNavigateBack: () -> Unit,
    detailViewModel: DetailViewModel = hiltViewModel()
) {
    val uiState by detailViewModel.uiState.collectAsState()
    val isDarkTheme by appViewModel.isDarkTheme.collectAsState()

    // 页面进入时加载数据
    LaunchedEffect(postId) {
        detailViewModel.loadPost(postId)
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("文章详情") },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "返回"
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                    titleContentColor = MaterialTheme.colorScheme.onPrimaryContainer,
                    navigationIconContentColor = MaterialTheme.colorScheme.onPrimaryContainer
                )
            )
        }
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            when {
                uiState.isLoading -> {
                    CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
                }
                uiState.errorMessage != null -> {
                    ErrorView(
                        message = uiState.errorMessage ?: "加载失败",
                        onRetry = { detailViewModel.retry(postId) }
                    )
                }
                uiState.post != null -> {
                    PostDetailContent(
                        post = uiState.post!!,
                        isDarkTheme = isDarkTheme
                    )
                }
            }
        }
    }
}

@Composable
private fun PostDetailContent(
    post: Post,
    isDarkTheme: Boolean
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // 主题状态提示
        ThemeBadge(isDarkTheme = isDarkTheme)

        // 文章标题
        Text(
            text = post.title,
            style = MaterialTheme.typography.headlineSmall,
            color = MaterialTheme.colorScheme.onBackground
        )

        HorizontalDivider(color = MaterialTheme.colorScheme.outlineVariant)

        // 元信息
        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.secondaryContainer
            )
        ) {
            Column(modifier = Modifier.padding(12.dp)) {
                InfoRow(label = "文章 ID", value = post.id.toString())
                Spacer(modifier = Modifier.height(4.dp))
                InfoRow(label = "作者 ID", value = post.userId.toString())
            }
        }

        // 正文
        Text(
            text = post.body,
            style = MaterialTheme.typography.bodyLarge,
            color = MaterialTheme.colorScheme.onBackground,
            lineHeight = MaterialTheme.typography.bodyLarge.lineHeight
        )
    }
}

@Composable
private fun ThemeBadge(isDarkTheme: Boolean) {
    Card(
        colors = CardDefaults.cardColors(
            containerColor = if (isDarkTheme) {
                MaterialTheme.colorScheme.tertiaryContainer
            } else {
                MaterialTheme.colorScheme.primaryContainer
            }
        )
    ) {
        Text(
            text = if (isDarkTheme) "🌙 当前：暗色主题" else "☀️ 当前：亮色主题",
            style = MaterialTheme.typography.labelMedium,
            modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp),
            color = if (isDarkTheme) {
                MaterialTheme.colorScheme.onTertiaryContainer
            } else {
                MaterialTheme.colorScheme.onPrimaryContainer
            }
        )
    }
}

@Composable
private fun InfoRow(label: String, value: String) {
    androidx.compose.foundation.layout.Row(
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Text(
            text = "$label：",
            style = MaterialTheme.typography.labelMedium,
            color = MaterialTheme.colorScheme.onSecondaryContainer
        )
        Text(
            text = value,
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSecondaryContainer
        )
    }
}

// ─── Preview（双主题） ────────────────────────────────────────────
@Preview(name = "DetailContent - Light", showBackground = true)
@Composable
private fun DetailLightPreview() {
    MilokTheme(darkTheme = false) {
        PostDetailContent(
            post = Post(1, 1, "示例文章标题，展示详情页样式", "这是文章的正文内容，展示了详情页的排版效果。"),
            isDarkTheme = false
        )
    }
}

@Preview(name = "DetailContent - Dark", showBackground = true, backgroundColor = 0xFF1C1B1F)
@Composable
private fun DetailDarkPreview() {
    MilokTheme(darkTheme = true) {
        PostDetailContent(
            post = Post(1, 1, "示例文章标题，展示详情页样式", "这是文章的正文内容，展示了详情页的排版效果。"),
            isDarkTheme = true
        )
    }
}
