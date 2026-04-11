package com.milok.app.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.milok.app.data.store.UserPreferencesStore
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * App 全局状态数据类
 */
data class AppUiState(
    val isDarkTheme: Boolean = false,
    val isLoggedIn: Boolean = false,
    val userName: String = "",
    val userToken: String? = null
)

/**
 * 全局 AppViewModel：管理主题模式、登录状态等跨页面共享状态
 * 通过 Hilt 注入，可在任意 Composable 中使用 hiltViewModel()
 */
@HiltViewModel
class AppViewModel @Inject constructor(
    private val preferencesStore: UserPreferencesStore
) : ViewModel() {

    private val _uiState = MutableStateFlow(AppUiState())
    val uiState: StateFlow<AppUiState> = _uiState.asStateFlow()

    // 直接暴露主题流，方便 MainActivity 订阅
    val isDarkTheme: StateFlow<Boolean> = preferencesStore.isDarkTheme
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.Eagerly,
            initialValue = false
        )

    val isLoggedIn: StateFlow<Boolean> = preferencesStore.isLoggedIn
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.Eagerly,
            initialValue = false
        )

    val userName: StateFlow<String> = preferencesStore.userName
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.Eagerly,
            initialValue = ""
        )

    // ─── 主题切换 ─────────────────────────────────────────────
    fun toggleTheme() {
        viewModelScope.launch {
            val current = isDarkTheme.value
            preferencesStore.setDarkTheme(!current)
        }
    }

    fun setDarkTheme(isDark: Boolean) {
        viewModelScope.launch {
            preferencesStore.setDarkTheme(isDark)
        }
    }

    // ─── 登录 / 退出 ──────────────────────────────────────────
    fun login(token: String = "mock_token_abc123", name: String = "Milok User") {
        viewModelScope.launch {
            preferencesStore.saveLoginInfo(token, name)
        }
    }

    fun logout() {
        viewModelScope.launch {
            preferencesStore.clearLoginInfo()
        }
    }
}
