package com.milok.app.data.network

import com.google.gson.annotations.SerializedName
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

// ─── Demo 响应数据模型 ───────────────────────────────────────────────
data class PostDto(
    @SerializedName("id")      val id: Int? = null,
    @SerializedName("userId")  val userId: Int? = null,
    @SerializedName("title")   val title: String? = null,
    @SerializedName("body")    val body: String? = null
)

data class UserDto(
    @SerializedName("id")       val id: Int? = null,
    @SerializedName("name")     val name: String? = null,
    @SerializedName("email")    val email: String? = null,
    @SerializedName("username") val username: String? = null
)

// ─── Retrofit Service 接口 ───────────────────────────────────────────
interface MilokApiService {

    @GET("posts")
    suspend fun getPosts(
        @Query("_limit") limit: Int = 20,
        @Query("_page") page: Int = 1
    ): Response<List<PostDto>>

    @GET("posts/{id}")
    suspend fun getPostById(
        @Path("id") id: Int
    ): Response<PostDto>

    @GET("users")
    suspend fun getUsers(): Response<List<UserDto>>

    @GET("users/{id}")
    suspend fun getUserById(
        @Path("id") id: Int
    ): Response<UserDto>
}
