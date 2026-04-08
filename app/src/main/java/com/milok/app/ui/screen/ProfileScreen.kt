package com.milok.app.ui.screen

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.milok.app.ui.theme.MilokTheme

@Composable
fun ProfileScreen() {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = "我的",
            style = MaterialTheme.typography.headlineMedium,
            color = MaterialTheme.colorScheme.primary
        )
    }
}

@Preview(name = "ProfileScreen - Light")
@Composable
private fun ProfileScreenLightPreview() {
    MilokTheme(darkTheme = false) {
        ProfileScreen()
    }
}

@Preview(name = "ProfileScreen - Dark", backgroundColor = 0xFF1C1B1F)
@Composable
private fun ProfileScreenDarkPreview() {
    MilokTheme(darkTheme = true) {
        ProfileScreen()
    }
}
