package com.milok.app.screen

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
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
import androidx.compose.ui.unit.dp
import com.milok.app.theme.MilokTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AIChatScreen() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
        
    ) {
        Text(
            text = "AI 聊天",
            style = MaterialTheme.typography.headlineMedium
        )

        Spacer(modifier = Modifier.height(16.dp))

        // 聊天内容区域
        Box(
            modifier = Modifier.weight(1f),
            contentAlignment = Alignment.Center,

        ) {
            Text("这里将来显示聊天记录")
        }
    }
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
