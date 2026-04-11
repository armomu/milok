package com.milok.app.screen

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.milok.app.theme.MilokTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AIChatScreen() {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Milok") },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                    titleContentColor = MaterialTheme.colorScheme.onPrimaryContainer
                )
            )
        },
        content = { paddingValues ->
            // 主要内容区域
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues), // 重要：应用 padding
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "AI 聊天内容",
                    style = MaterialTheme.typography.headlineMedium
                )
            }
        }
    )
}

@Preview(name = "AIChatScreen - Light")
@Composable
private fun AIChatScreenLightPreview() {
    MilokTheme(darkTheme = false) {
        AIChatScreen()
    }
}

@Preview(name = "AIChatScreen - Dark", backgroundColor = 0xFF1C1B1F)
@Composable
private fun AIChatScreenDarkPreview() {
    MilokTheme(darkTheme = true) {
        AIChatScreen()
    }
}
