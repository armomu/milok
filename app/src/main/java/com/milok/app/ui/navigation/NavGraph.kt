package com.milok.app.ui.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.milok.app.ui.screen.DetailScreen
import com.milok.app.ui.screen.MainScreen
import com.milok.app.ui.screen.SettingsScreen
import com.milok.app.ui.viewmodel.AppViewModel

/**
 * 应用路由图
 *
 * @param navController         导航控制器
 * @param appViewModel          全局状态 ViewModel
 * @param startDestination      起始路由
 */
@Composable
fun MilokNavGraph(
    navController: NavHostController,
    appViewModel: AppViewModel,
    startDestination: String = Screen.Main.route
) {
    NavHost(
        navController = navController,
        startDestination = startDestination
    ) {
        // ─── 主容器（含底部 Tab） ──────────────────────────────
        composable(route = Screen.Main.route) {
            MainScreen(
                appViewModel = appViewModel,
                onNavigateToDetail = { postId ->
                    navController.navigate(Screen.Detail.createRoute(postId))
                },
                onNavigateToSettings = {
                    navController.navigate(Screen.Settings.route)
                }
            )
        }

        // ─── 详情页（带路由参数） ───────────────────────────────
        composable(
            route = Screen.Detail.route,
            arguments = listOf(
                navArgument(Screen.Detail.ARG_POST_ID) {
                    type = NavType.IntType
                }
            )
        ) { backStackEntry ->
            val postId = backStackEntry.arguments?.getInt(Screen.Detail.ARG_POST_ID) ?: 0
            DetailScreen(
                postId = postId,
                appViewModel = appViewModel,
                onNavigateBack = { navController.popBackStack() }
            )
        }

        // ─── 设置页 ────────────────────────────────────────────
        composable(route = Screen.Settings.route) {
            SettingsScreen(
                appViewModel = appViewModel,
                onNavigateBack = { navController.popBackStack() }
            )
        }
    }
}
