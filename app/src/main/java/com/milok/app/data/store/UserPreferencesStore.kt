package com.milok.app.data.store

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

// DataStore 单例扩展
private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "milok_prefs")

/**
 * 用户偏好设置持久化 Store
 * - 主题模式（暗黑 / 亮色）
 * - 登录状态
 * - 用户 Token
 */
@Singleton
class UserPreferencesStore @Inject constructor(
    @ApplicationContext private val context: Context
) {

    companion object {
        val KEY_IS_DARK_THEME = booleanPreferencesKey("is_dark_theme")
        val KEY_IS_LOGGED_IN = booleanPreferencesKey("is_logged_in")
        val KEY_USER_TOKEN = stringPreferencesKey("user_token")
        val KEY_USER_NAME = stringPreferencesKey("user_name")
    }

    // ─── Theme ───────────────────────────────────────────────
    val isDarkTheme: Flow<Boolean> = context.dataStore.data
        .map { prefs -> prefs[KEY_IS_DARK_THEME] ?: false }

    suspend fun setDarkTheme(isDark: Boolean) {
        context.dataStore.edit { prefs ->
            prefs[KEY_IS_DARK_THEME] = isDark
        }
    }

    // ─── Login ───────────────────────────────────────────────
    val isLoggedIn: Flow<Boolean> = context.dataStore.data
        .map { prefs -> prefs[KEY_IS_LOGGED_IN] ?: false }

    val userToken: Flow<String?> = context.dataStore.data
        .map { prefs -> prefs[KEY_USER_TOKEN] }

    val userName: Flow<String> = context.dataStore.data
        .map { prefs -> prefs[KEY_USER_NAME] ?: "" }

    suspend fun saveLoginInfo(token: String, name: String) {
        context.dataStore.edit { prefs ->
            prefs[KEY_IS_LOGGED_IN] = true
            prefs[KEY_USER_TOKEN] = token
            prefs[KEY_USER_NAME] = name
        }
    }

    suspend fun clearLoginInfo() {
        context.dataStore.edit { prefs ->
            prefs[KEY_IS_LOGGED_IN] = false
            prefs.remove(KEY_USER_TOKEN)
            prefs.remove(KEY_USER_NAME)
        }
    }
}
