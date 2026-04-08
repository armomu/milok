package com.milok.app.data.network

import retrofit2.Response

/**
 * 安全执行网络请求，统一捕获异常并包装成 Resource
 */
suspend fun <T> safeApiCall(
    apiCall: suspend () -> Response<T>
): Resource<T> {
    return try {
        val response = apiCall()
        if (response.isSuccessful) {
            val body = response.body()
            if (body != null) {
                Resource.Success(body)
            } else {
                Resource.Error("响应体为空", response.code())
            }
        } else {
            val errorMsg = response.errorBody()?.string() ?: "请求失败"
            Resource.Error(errorMsg, response.code())
        }
    } catch (e: java.net.UnknownHostException) {
        Resource.Error("网络不可用，请检查网络连接", throwable = e)
    } catch (e: java.net.SocketTimeoutException) {
        Resource.Error("请求超时，请重试", throwable = e)
    } catch (e: Exception) {
        Resource.Error(e.message ?: "未知错误", throwable = e)
    }
}

/**
 * 针对直接返回 T（非 Response<T>）的 suspend 函数做安全包装
 */
suspend fun <T> safeCall(
    call: suspend () -> T
): Resource<T> {
    return try {
        Resource.Success(call())
    } catch (e: java.net.UnknownHostException) {
        Resource.Error("网络不可用，请检查网络连接", throwable = e)
    } catch (e: java.net.SocketTimeoutException) {
        Resource.Error("请求超时，请重试", throwable = e)
    } catch (e: Exception) {
        Resource.Error(e.message ?: "未知错误", throwable = e)
    }
}
