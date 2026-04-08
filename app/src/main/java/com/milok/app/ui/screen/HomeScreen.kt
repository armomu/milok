package com.milok.app.ui.screen

import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.milok.app.data.model.Post
import com.milok.app.ui.components.ErrorView
import com.milok.app.ui.theme.MilokTheme
import com.milok.app.ui.viewmodel.AppViewModel
import com.milok.app.ui.viewmodel.HomeViewModel

@Composable
fun HomeScreen(
    appViewModel: AppViewModel,
    onNavigateToDetail: (Int) -> Unit,
    modifier: Modifier = Modifier,
    homeViewModel: HomeViewModel = hiltViewModel()
) {
    val homeState by homeViewModel.uiState.collectAsState()
    val isLoggedIn by appViewModel.isLoggedIn.collectAsState()
    val userName by appViewModel.userName.collectAsState()

    Column(
        modifier = modifier.fillMaxSize()
    ) {
        // 登录状态横条
        LoginBanner(
            isLoggedIn = isLoggedIn,
            userName = userName,
            onLogin = { appViewModel.login() },
            onLogout = { appViewModel.logout() }
        )

        when {
            homeState.isLoading -> {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator()
                }
            }
            homeState.errorMessage != null -> {
                ErrorView(
                    message = homeState.errorMessage ?: "未知错误",
                    onRetry = { homeViewModel.retry() }
                )
            }
            else -> {
                LazyColumn(
                    contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    items(homeState.posts, key = { it.id }) { post ->
                        PostCard(
                            post = post,
                            onClick = { onNavigateToDetail(post.id) }
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun LoginBanner(
    isLoggedIn: Boolean,
    userName: String,
    onLogin: () -> Unit,
    onLogout: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(
                imageVector = Icons.Filled.Person,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.primary
            )
            Spacer(modifier = Modifier.padding(horizontal = 4.dp))
            Text(
                text = if (isLoggedIn) "你好，$userName" else "未登录",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurface
            )
        }
        Button(
            onClick = if (isLoggedIn) onLogout else onLogin
        ) {
            Text(if (isLoggedIn) "退出" else "登录")
        }
    }
}

@Composable
private fun PostCard(
    post: Post,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant
        )
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                text = "# ${post.id} ${post.title}",
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis
            )
            Spacer(modifier = Modifier.height(6.dp))
            Text(
                text = post.body,
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                maxLines = 3,
                overflow = TextOverflow.Ellipsis
            )
        }
    }
}

// ─── Preview ─────────────────────────────────────────────────────
@Preview(name = "HomeScreen - Light", showBackground = true)
@Composable
private fun HomeScreenLightPreview() {
    MilokTheme(darkTheme = false) {
        Column {
            PostCard(
                post = Post(1, 1, "这是一篇示例文章标题", "这里是文章正文内容，可能有很多文字..."),
                onClick = {}
            )
        }
    }
}

@Preview(name = "HomeScreen - Dark", showBackground = true, backgroundColor = 0xFF1C1B1F)
@Composable
private fun HomeScreenDarkPreview() {
    MilokTheme(darkTheme = true) {
        Column {
            PostCard(
                post = Post(2, 1, "深色模式示例文章", "深色模式下的文章正文内容..."),
                onClick = {}
            )
        }
    }
}
