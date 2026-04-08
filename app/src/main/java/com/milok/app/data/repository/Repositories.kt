package com.milok.app.data.repository

import com.milok.app.data.model.Post
import com.milok.app.data.model.User
import com.milok.app.data.network.Resource

/**
 * Post 仓库接口
 */
interface PostRepository {
    suspend fun getPosts(limit: Int = 20, page: Int = 1): Resource<List<Post>>
    suspend fun getPostById(id: Int): Resource<Post>
}

/**
 * User 仓库接口
 */
interface UserRepository {
    suspend fun getUsers(): Resource<List<User>>
    suspend fun getUserById(id: Int): Resource<User>
}
