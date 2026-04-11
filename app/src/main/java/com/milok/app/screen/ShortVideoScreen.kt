package com.milok.app.screen

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.milok.app.theme.MilokTheme

@Composable
fun ShortVideoScreen() {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = "短视频",
            style = MaterialTheme.typography.headlineMedium,
            color = MaterialTheme.colorScheme.primary
        )
    }
}

@Preview(name = "ShortVideoScreen - Light")
@Composable
private fun ShortVideoScreenLightPreview() {
    MilokTheme(darkTheme = false) {
        ShortVideoScreen()
    }
}

@Preview(name = "ShortVideoScreen - Dark", backgroundColor = 0xFF1C1B1F)
@Composable
private fun ShortVideoScreenDarkPreview() {
    MilokTheme(darkTheme = true) {
        ShortVideoScreen()
    }
}
