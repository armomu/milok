# Android 原生示例项目 纯AI生成

基于 **Android Studio Hedgehog (2023.1.1)** 创建的 Kotlin + Jetpack Compose 原生 Android 项目，展示现代 Android 架构最佳实践。

---

## 技术栈

| 分类 | 技术 |
|------|------|
| UI | Jetpack Compose + Material Design 3 |
| 语言 | Kotlin |
| 架构 | MVVM + Clean Architecture |
| 依赖注入 | Hilt |
| 导航 | Compose Navigation |
| 网络 | Retrofit2 + OkHttp3 |
| 持久化 | DataStore Preferences |
| 图片加载 | Coil |
| 协程 | Kotlin Coroutines + Flow |

---

## 项目结构

```
app/src/main/java/com/milok/app/
├── MilokApplication.kt          # Hilt Application 入口
├── MainActivity.kt              # 主 Activity，持有全局主题状态
│
├── data/                        # 数据层
│   ├── model/
│   │   ├── Models.kt            # 领域模型（Post、User）
│   │   └── Mappers.kt           # DTO → 领域模型映射
│   ├── network/
│   │   ├── Resource.kt          # 统一状态封装 Loading/Success/Error
│   │   ├── ApiResponse.kt       # 统一响应结构体
│   │   ├── ApiService.kt        # Retrofit 接口定义
│   │   ├── Interceptors.kt      # OkHttp 拦截器（Auth、Header、ResponseCode）
│   │   └── SafeApiCall.kt       # 安全请求包装工具函数
│   ├── repository/
│   │   ├── Repositories.kt      # Repository 接口
│   │   ├── PostRepositoryImpl.kt
│   │   └── UserRepositoryImpl.kt
│   └── store/
│       └── UserPreferencesStore.kt  # DataStore 持久化
│
├── di/                          # Hilt 依赖注入模块
│   ├── NetworkModule.kt         # OkHttp + Retrofit 提供
│   └── RepositoryModule.kt      # Repository 绑定
│
├── ui/                          # UI 层
│   ├── components/              # 可复用组件
│   │   ├── ErrorView.kt
│   │   └── LoadingView.kt
│   ├── navigation/
│   │   ├── Screen.kt            # 路由定义（含参数化路由）
│   │   └── NavGraph.kt          # NavHost 路由图
│   ├── screen/                  # 页面
│   │   ├── HomeScreen.kt        # 首页（文章列表 + 登录状态）
│   │   ├── DetailScreen.kt      # 详情页（路由传参 postId）
│   │   └── SettingsScreen.kt    # 设置页（主题切换 + 账号管理）
│   ├── theme/
│   │   ├── MilokTheme.kt        # 双主题（亮/暗）ColorScheme
│   │   ├── Typography.kt        # M3 字体排版
│   │   └── Shapes.kt            # 圆角 Shapes
│   └── viewmodel/
│       ├── AppViewModel.kt      # 全局状态（主题 + 登录）
│       ├── HomeViewModel.kt     # 首页 ViewModel
│       └── DetailViewModel.kt   # 详情页 ViewModel
│
└── utils/                       # 工具类
    ├── Utils.kt                 # Toast / Date / String / Network 工具
    └── Extensions.kt            # Kotlin 扩展函数
```

---

## 核心特性

### 1. 统一网络层

```kotlin
// Resource<T> 统一状态
sealed class Resource<out T> {
    object Loading : Resource<Nothing>()
    data class Success<T>(val data: T) : Resource<T>()
    data class Error(val message: String, val code: Int = -1) : Resource<Nothing>()
}

// safeApiCall 自动捕获网络异常
val result = safeApiCall { apiService.getPosts() }
result.onSuccess { posts -> /* 处理数据 */ }
     .onError { msg, code -> /* 处理错误 */ }
```

### 2. 手动主题切换 + DataStore 持久化

```kotlin
// AppViewModel 中切换主题
fun toggleTheme() {
    viewModelScope.launch {
        preferencesStore.setDarkTheme(!isDarkTheme.value)
    }
}

// MainActivity 中响应主题变化
val isDarkTheme by appViewModel.isDarkTheme.collectAsState()
MilokTheme(darkTheme = isDarkTheme) { /* ... */ }
```

### 3. Compose Navigation 传参

```kotlin
// 定义路由
object Detail : Screen("detail/{postId}") {
    fun createRoute(postId: Int) = "detail/$postId"
}

// 导航时传参
navController.navigate(Screen.Detail.createRoute(postId))

// 目标页面接收参数
composable(
    route = Screen.Detail.route,
    arguments = listOf(navArgument("postId") { type = NavType.IntType })
) { backStackEntry ->
    val postId = backStackEntry.arguments?.getInt("postId") ?: 0
    DetailScreen(postId = postId, ...)
}
```

---

## 快速开始

1. 使用 **Android Studio Hedgehog (2023.1.1)** 打开项目根目录
2. 等待 Gradle Sync 完成
3. 选择 `app` 模块，点击 Run（或 `Shift+F10`）
4. Demo 使用 `https://jsonplaceholder.typicode.com` 作为测试 API

---

## Build 信息

- **compileSdk**: 34
- **targetSdk**: 34  
- **minSdk**: 26 (Android 8.0+)
- **Gradle**: 8.2
- **AGP**: 8.2.2
- **Kotlin**: 1.9.22
