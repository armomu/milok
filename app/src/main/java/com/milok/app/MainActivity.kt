package com.milok.app

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.compose.rememberNavController
import com.milok.app.navigation.MilokNavGraph
import com.milok.app.theme.MilokTheme
import com.milok.app.viewmodel.AppViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContent {
            // 全局 AppViewModel - 由 Hilt 管理，跨页面共享
            val appViewModel: AppViewModel = hiltViewModel()
            val isDarkTheme by appViewModel.isDarkTheme.collectAsState()

            MilokTheme(darkTheme = isDarkTheme) {
                val navController = rememberNavController()
                MilokNavGraph(
                    navController = navController,
                    appViewModel = appViewModel
                )
            }
        }
    }
}
