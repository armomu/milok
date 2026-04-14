package com.milok.app.screen

import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Chat
import androidx.compose.material.icons.filled.DarkMode
import androidx.compose.material.icons.filled.LightMode
import androidx.compose.material.icons.filled.List
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.VideoLibrary
import androidx.compose.material.icons.outlined.Chat
import androidx.compose.material.icons.outlined.List
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material.icons.outlined.VideoLibrary
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import com.milok.app.viewmodel.AppViewModel

/**
 * 底部 Tab 数据
 */
private data class TabItem(
    val title: String,
    val selectedIcon: ImageVector,
    val unselectedIcon: ImageVector
)

/**
 * 应用主容器：顶部栏 + 底部 Tab 导航
 * 4 个 Tab：列表、短视频、AI 聊天、我的
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen(
    appViewModel: AppViewModel,
    onNavigateToDetail: (Int) -> Unit,
    onNavigateToSettings: () -> Unit,
    onNavigateToScan: () -> Unit
) {
    val isDarkTheme by appViewModel.isDarkTheme.collectAsState()

    val tabs = listOf(
        TabItem("列表", Icons.Filled.List, Icons.Outlined.List),
        TabItem("短视频", Icons.Filled.VideoLibrary, Icons.Outlined.VideoLibrary),
        TabItem("AI 聊天", Icons.Filled.Chat, Icons.Outlined.Chat),
        TabItem("我的", Icons.Filled.Person, Icons.Outlined.Person)
    )

    // 用 rememberSaveable 保证 Tab 切换状态在配置变更后不丢失
    var selectedTabIndex by rememberSaveable { mutableIntStateOf(0) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Milok · ${tabs[selectedTabIndex].title}") },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                    titleContentColor = MaterialTheme.colorScheme.onPrimaryContainer
                ),
                actions = {
                    // 主题切换按钮
                    IconButton(onClick = { appViewModel.toggleTheme() }) {
                        Icon(
                            imageVector = if (isDarkTheme) Icons.Filled.LightMode else Icons.Filled.DarkMode,
                            contentDescription = if (isDarkTheme) "切换到亮色" else "切换到暗色"
                        )
                    }
                    // 设置按钮
                    IconButton(onClick = onNavigateToSettings) {
                        Icon(
                            imageVector = Icons.Filled.Settings,
                            contentDescription = "设置"
                        )
                    }
                }
            )
        },
        bottomBar = {
            NavigationBar {
                tabs.forEachIndexed { index, tab ->
                    NavigationBarItem(
                        selected = selectedTabIndex == index,
                        onClick = { selectedTabIndex = index },
                        icon = {
                            Icon(
                                imageVector = if (selectedTabIndex == index) tab.selectedIcon else tab.unselectedIcon,
                                contentDescription = tab.title
                            )
                        },
                        label = { Text(tab.title) }
                    )
                }
            }
        }
    ) { paddingValues ->
        // 根据选中 Tab 展示对应页面
        when (selectedTabIndex) {
            0 -> HomeTabContent(
                appViewModel = appViewModel,
                onNavigateToDetail = onNavigateToDetail,
                modifier = Modifier.padding(paddingValues),
                onNavigateToScan = onNavigateToScan
            )
            1 -> ShortVideoScreen()
            2 -> AIChatScreen()
            3 -> ProfileScreen(
                onNavigateToSettings = onNavigateToSettings
            )
        }
    }
}

/**
 * 首页列表 Tab 内容（从原 HomeScreen 提取）
 */
@Composable
private fun HomeTabContent(
    appViewModel: AppViewModel,
    onNavigateToDetail: (Int) -> Unit,
    modifier: Modifier = Modifier,
    onNavigateToScan:  () -> Unit
) {
    HomeScreen(
        appViewModel = appViewModel,
        onNavigateToDetail = onNavigateToDetail,
        modifier = modifier,
        onNavigateToScan = onNavigateToScan
    )
}
