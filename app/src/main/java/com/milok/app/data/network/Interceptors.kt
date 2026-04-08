package com.milok.app.data.network

import android.util.Log
import okhttp3.Interceptor
import okhttp3.Request
import okhttp3.Response

private const val TAG = "AuthInterceptor"

/**
 * 认证拦截器：自动为每个请求添加 Authorization 头
 * Token 由外部注入（构造函数或 Provider）
 */
class AuthInterceptor(
    private val tokenProvider: () -> String?
) : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        val originalRequest: Request = chain.request()
        val token = tokenProvider()

        val newRequest = if (token != null) {
            originalRequest.newBuilder()
                .header("Authorization", "Bearer $token")
                .build()
        } else {
            originalRequest
        }

        Log.i(TAG, "intercept: [${newRequest.method}] ${newRequest.url}")
        return chain.proceed(newRequest)
    }
}

/**
 * 公共请求头拦截器：添加 Content-Type、Accept、App-Version 等通用头
 */
class CommonHeaderInterceptor(
    private val appVersion: String = "1.0.0"
) : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request().newBuilder()
            .header("Content-Type", "application/json")
            .header("Accept", "application/json")
            .header("X-App-Version", appVersion)
            .header("X-Platform", "Android")
            .build()
        return chain.proceed(request)
    }
}

/**
 * 响应码拦截器：统一处理 401 未授权、5xx 服务端错误等情况
 */
class ResponseCodeInterceptor : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        val response = chain.proceed(chain.request())

        when (response.code) {
            401 -> Log.w(TAG, "intercept: 401 Unauthorized - token may be expired")
            403 -> Log.w(TAG, "intercept: 403 Forbidden")
            500, 502, 503 -> Log.e(TAG, "intercept: Server error ${response.code}")
            else -> Unit
        }

        return response
    }
}
