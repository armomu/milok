package com.milok.app.data.repository

import com.milok.app.data.model.User
import com.milok.app.data.model.toDomain
import com.milok.app.data.network.MilokApiService
import com.milok.app.data.network.Resource
import com.milok.app.data.network.safeApiCall
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class UserRepositoryImpl @Inject constructor(
    private val apiService: MilokApiService
) : UserRepository {

    override suspend fun getUsers(): Resource<List<User>> {
        return safeApiCall { apiService.getUsers() }.let { resource ->
            when (resource) {
                is Resource.Success -> Resource.Success(resource.data.toDomain())
                is Resource.Error -> resource
                is Resource.Loading -> resource
            }
        }
    }

    override suspend fun getUserById(id: Int): Resource<User> {
        return safeApiCall { apiService.getUserById(id) }.let { resource ->
            when (resource) {
                is Resource.Success -> Resource.Success(resource.data.toDomain())
                is Resource.Error -> resource
                is Resource.Loading -> resource
            }
        }
    }
}
