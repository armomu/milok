package com.milok.app.ui.navigation

/**
 * 路由目的地定义
 */
sealed class Screen(val route: String) {

    /** 首页（底部 Tab 容器） */
    object Main : Screen("main")

    /** 首页 - 列表 Tab */
    object Home : Screen("home")

    /** 短视频 Tab */
    object ShortVideo : Screen("short_video")

    /** AI 聊天 Tab */
    object AIChat : Screen("ai_chat")

    /** 我的 Tab */
    object Profile : Screen("profile")

    /** 详情页，携带 postId 参数 */
    object Detail : Screen("detail/{postId}") {
        const val ARG_POST_ID = "postId"
        fun createRoute(postId: Int) = "detail/$postId"
    }

    /** 设置页 */
    object Settings : Screen("settings")
}
