package com.milok.app

import android.app.Application
import dagger.hilt.android.HiltAndroidApp

/**
 * Milok Application
 * 必须加 @HiltAndroidApp 以启用 Hilt 依赖注入
 */
@HiltAndroidApp
class MilokApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        // 应用级初始化（如日志框架、崩溃上报等）
    }
}
