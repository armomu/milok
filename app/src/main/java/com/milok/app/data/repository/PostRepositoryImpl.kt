package com.milok.app.data.repository

import com.milok.app.data.model.Post
import com.milok.app.data.model.toDomain
import com.milok.app.data.network.MilokApiService
import com.milok.app.data.network.Resource
import com.milok.app.data.network.safeApiCall
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class PostRepositoryImpl @Inject constructor(
    private val apiService: MilokApiService
) : PostRepository {

    override suspend fun getPosts(limit: Int, page: Int): Resource<List<Post>> {
        return safeApiCall { apiService.getPosts(limit, page) }.let { resource ->
            when (resource) {
                is Resource.Success -> Resource.Success(resource.data.toDomain())
                is Resource.Error -> resource
                is Resource.Loading -> resource
            }
        }
    }

    override suspend fun getPostById(id: Int): Resource<Post> {
        return safeApiCall { apiService.getPostById(id) }.let { resource ->
            when (resource) {
                is Resource.Success -> Resource.Success(resource.data.toDomain())
                is Resource.Error -> resource
                is Resource.Loading -> resource
            }
        }
    }
}
