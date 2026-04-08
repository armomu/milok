package com.milok.app.data.network

/**
 * 统一响应封装：涵盖 Loading / Success / Error 三种状态
 */
sealed class Resource<out T> {

    /** 加载中 */
    object Loading : Resource<Nothing>()

    /** 成功，携带数据 */
    data class Success<T>(val data: T) : Resource<T>()

    /** 失败，携带错误信息 */
    data class Error(
        val message: String,
        val code: Int = -1,
        val throwable: Throwable? = null
    ) : Resource<Nothing>()

    // ------------------- 扩展属性 -------------------

    val isLoading get() = this is Loading
    val isSuccess get() = this is Success
    val isError get() = this is Error

    val dataOrNull: T? get() = (this as? Success)?.data
    val errorMessage: String? get() = (this as? Error)?.message

    // ------------------- 扩展函数 -------------------

    fun onLoading(block: () -> Unit): Resource<T> {
        if (this is Loading) block()
        return this
    }

    fun onSuccess(block: (T) -> Unit): Resource<T> {
        if (this is Success) block(data)
        return this
    }

    fun onError(block: (String, Int) -> Unit): Resource<T> {
        if (this is Error) block(message, code)
        return this
    }
}
