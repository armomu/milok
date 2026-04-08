package com.milok.app.ui.navigation

/**
 * 路由目的地定义
 */
sealed class Screen(val route: String) {

    /** 首页 */
    object Home : Screen("home")

    /** 详情页，携带 postId 参数 */
    object Detail : Screen("detail/{postId}") {
        const val ARG_POST_ID = "postId"
        fun createRoute(postId: Int) = "detail/$postId"
    }

    /** 设置页 */
    object Settings : Screen("settings")
}
