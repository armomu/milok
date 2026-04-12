package com.milok.app.data.repository

import com.milok.app.data.model.Station
import com.milok.app.data.model.toDomain
import com.milok.app.data.network.MilokApiService
import com.milok.app.data.network.Resource
import com.milok.app.data.network.safeApiCall
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class StationRepositoryImpl @Inject constructor(
    private val apiService: MilokApiService
) : StationRepository {

    override suspend fun getStations(
        page: Int,
        pageSize: Int,
        name: String?
    ): Resource<PagedResult<Station>> {
        val result = safeApiCall { apiService.getStations(page, pageSize, name) }
        return when (result) {
            is Resource.Success -> {
                val apiResponse = result.data
                if (apiResponse.code != 0) {
                    Resource.Error(apiResponse.message ?: "请求失败")
                } else {
                    val pagedData = apiResponse.data
                    val stations = pagedData?.data?.map { it.toDomain() } ?: emptyList()
                    val total = pagedData?.total ?: 0
                    Resource.Success(PagedResult(items = stations, total = total))
                }
            }
            is Resource.Error -> result
            is Resource.Loading -> result
        }
    }
}
