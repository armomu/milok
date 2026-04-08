package com.milok.app.data.network

import com.google.gson.annotations.SerializedName

/**
 * 统一 API 响应结构体
 * 实际项目中根据后端约定调整字段名
 */
data class ApiResponse<T>(
    @SerializedName("code")
    val code: Int? = null,

    @SerializedName("message")
    val message: String? = null,

    @SerializedName("data")
    val data: T? = null
) {
    val isSuccessful: Boolean get() = code == 0 || code == 200
}

/**
 * 将 ApiResponse 包装转换为 Resource
 */
fun <T> ApiResponse<T>.toResource(): Resource<T> {
    return if (isSuccessful && data != null) {
        Resource.Success(data)
    } else {
        Resource.Error(
            message = message ?: "Unknown error",
            code = code ?: -1
        )
    }
}
