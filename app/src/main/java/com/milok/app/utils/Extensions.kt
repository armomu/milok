package com.milok.app.utils

import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.collectAsState
import kotlinx.coroutines.flow.StateFlow

/**
 * 将 StateFlow 收集为 Compose State 的扩展函数
 */
@Composable
fun <T> StateFlow<T>.collectAsStateWithLifecycle(): State<T> {
    return this.collectAsState()
}

/**
 * 安全除法（避免除以零）
 */
fun Int.safeDivide(divisor: Int, default: Int = 0): Int {
    return if (divisor == 0) default else this / divisor
}

/**
 * 列表为空时返回 null
 */
fun <T> List<T>.nullIfEmpty(): List<T>? = if (isEmpty()) null else this
