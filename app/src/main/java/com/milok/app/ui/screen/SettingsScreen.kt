package com.milok.app.ui.screen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.DarkMode
import androidx.compose.material.icons.filled.LightMode
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.milok.app.BuildConfig
import com.milok.app.ui.theme.MilokTheme
import com.milok.app.ui.viewmodel.AppViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsScreen(
    appViewModel: AppViewModel,
    onNavigateBack: () -> Unit
) {
    val isDarkTheme by appViewModel.isDarkTheme.collectAsState()
    val isLoggedIn by appViewModel.isLoggedIn.collectAsState()
    val userName by appViewModel.userName.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("设置") },
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
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .verticalScroll(rememberScrollState())
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // ─── 外观设置区 ────────────────────────────────────
            SettingsSectionHeader(title = "外观")

            SettingsThemeCard(
                isDarkTheme = isDarkTheme,
                onToggle = { appViewModel.toggleTheme() },
                onSetLight = { appViewModel.setDarkTheme(false) },
                onSetDark = { appViewModel.setDarkTheme(true) }
            )

            HorizontalDivider(color = MaterialTheme.colorScheme.outlineVariant)

            // ─── 账号区 ────────────────────────────────────────
            SettingsSectionHeader(title = "账号")

            SettingsAccountCard(
                isLoggedIn = isLoggedIn,
                userName = userName,
                onLogin = { appViewModel.login() },
                onLogout = { appViewModel.logout() }
            )

            HorizontalDivider(color = MaterialTheme.colorScheme.outlineVariant)

            // ─── 关于区 ────────────────────────────────────────
            SettingsSectionHeader(title = "关于")

            SettingsInfoCard()
        }
    }
}

@Composable
private fun SettingsSectionHeader(title: String) {
    Text(
        text = title,
        style = MaterialTheme.typography.titleSmall,
        color = MaterialTheme.colorScheme.primary,
        modifier = Modifier.padding(start = 4.dp)
    )
}

@Composable
private fun SettingsThemeCard(
    isDarkTheme: Boolean,
    onToggle: () -> Unit,
    onSetLight: () -> Unit,
    onSetDark: () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant
        )
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            // Switch 开关
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Icon(
                        imageVector = if (isDarkTheme) Icons.Filled.DarkMode else Icons.Filled.LightMode,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.primary
                    )
                    Column {
                        Text(
                            text = "暗色模式",
                            style = MaterialTheme.typography.bodyLarge,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                        Text(
                            text = if (isDarkTheme) "已开启" else "已关闭",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.7f)
                        )
                    }
                }
                Switch(
                    checked = isDarkTheme,
                    onCheckedChange = { onToggle() },
                    colors = SwitchDefaults.colors(
                        checkedThumbColor = MaterialTheme.colorScheme.primary,
                        checkedTrackColor = MaterialTheme.colorScheme.primaryContainer
                    )
                )
            }

            Spacer(modifier = Modifier.height(12.dp))

            // 快捷按钮区
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                androidx.compose.material3.OutlinedButton(
                    onClick = onSetLight,
                    modifier = Modifier.weight(1f),
                    enabled = isDarkTheme
                ) {
                    Icon(
                        imageVector = Icons.Filled.LightMode,
                        contentDescription = null,
                        modifier = Modifier.padding(end = 4.dp)
                    )
                    Text("亮色")
                }
                androidx.compose.material3.OutlinedButton(
                    onClick = onSetDark,
                    modifier = Modifier.weight(1f),
                    enabled = !isDarkTheme
                ) {
                    Icon(
                        imageVector = Icons.Filled.DarkMode,
                        contentDescription = null,
                        modifier = Modifier.padding(end = 4.dp)
                    )
                    Text("暗色")
                }
            }
        }
    }
}

@Composable
private fun SettingsAccountCard(
    isLoggedIn: Boolean,
    userName: String,
    onLogin: () -> Unit,
    onLogout: () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant
        )
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                text = if (isLoggedIn) "当前用户：$userName" else "未登录",
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            Spacer(modifier = Modifier.height(12.dp))
            androidx.compose.material3.Button(
                onClick = if (isLoggedIn) onLogout else onLogin,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(if (isLoggedIn) "退出登录" else "模拟登录")
            }
        }
    }
}

@Composable
private fun SettingsInfoCard() {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant
        )
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            SettingsInfoRow(label = "应用名称", value = "Milok")
            Spacer(modifier = Modifier.height(4.dp))
            SettingsInfoRow(label = "版本", value = BuildConfig.VERSION_NAME)
            Spacer(modifier = Modifier.height(4.dp))
            SettingsInfoRow(label = "Build Type", value = BuildConfig.BUILD_TYPE)
            Spacer(modifier = Modifier.height(4.dp))
            SettingsInfoRow(label = "技术栈", value = "Kotlin + Jetpack Compose")
            Spacer(modifier = Modifier.height(4.dp))
            SettingsInfoRow(label = "DI 框架", value = "Hilt")
        }
    }
}

@Composable
private fun SettingsInfoRow(label: String, value: String) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            text = label,
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
        Text(
            text = value,
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.7f)
        )
    }
}

// ─── 双主题 Preview ───────────────────────────────────────────────
@Preview(name = "SettingsThemeCard - Light", showBackground = true)
@Composable
private fun SettingsLightPreview() {
    MilokTheme(darkTheme = false) {
        Column(modifier = Modifier.padding(16.dp)) {
            SettingsThemeCard(
                isDarkTheme = false,
                onToggle = {},
                onSetLight = {},
                onSetDark = {}
            )
        }
    }
}

@Preview(name = "SettingsThemeCard - Dark", showBackground = true, backgroundColor = 0xFF1C1B1F)
@Composable
private fun SettingsDarkPreview() {
    MilokTheme(darkTheme = true) {
        Column(modifier = Modifier.padding(16.dp)) {
            SettingsThemeCard(
                isDarkTheme = true,
                onToggle = {},
                onSetLight = {},
                onSetDark = {}
            )
        }
    }
}
