package com.milok.app.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.milok.app.data.model.Station
import com.milok.app.data.network.Resource
import com.milok.app.data.repository.StationRepository
import kotlinx.coroutines.launch

private const val PAGE_SIZE = 6

data class HomeUiState(
    val stations: List<Station> = emptyList(),
    val isLoading: Boolean = false,
    val isRefreshing: Boolean = false,
    val isLoadingMore: Boolean = false,
    val errorMessage: String? = null,
    val currentPage: Int = 1,
    val totalCount: Int = 0,
    val searchName: String = "",
    val hasMore: Boolean = false
)

class HomeViewModel(
    private val stationRepository: StationRepository
) : ViewModel() {

    var uiState by mutableStateOf(HomeUiState())
        private set

    init {
        loadStations(page = 1, replace = true)
    }

    /** 首次加载 / 搜索触发的加载（替换列表） */
    private fun loadStations(page: Int, replace: Boolean, name: String? = null) {
        viewModelScope.launch {
            val searchName = name ?: uiState.searchName.takeIf { it.isNotBlank() }

            if (replace) {
                uiState = uiState.copy(isLoading = true, errorMessage = null)
            }

            when (val result = stationRepository.getStations(
                page = page,
                pageSize = PAGE_SIZE,
                name = searchName
            )) {
                is Resource.Success -> {
                    val pagedResult = result.data
                    val newStations = if (replace) {
                        pagedResult.items
                    } else {
                        uiState.stations + pagedResult.items
                    }
                    val hasMore = newStations.size < pagedResult.total
                    uiState = uiState.copy(
                        stations = newStations,
                        isLoading = false,
                        isRefreshing = false,
                        isLoadingMore = false,
                        currentPage = page,
                        totalCount = pagedResult.total,
                        searchName = searchName ?: "",
                        hasMore = hasMore,
                        errorMessage = null
                    )
                }
                is Resource.Error -> {
                    uiState = uiState.copy(
                        isLoading = false,
                        isRefreshing = false,
                        isLoadingMore = false,
                        errorMessage = result.message
                    )
                }
                is Resource.Loading -> Unit
            }
        }
    }

    /** 下拉刷新：重置到第 1 页 */
    fun refresh() {
        uiState = uiState.copy(isRefreshing = true, errorMessage = null)
        loadStations(page = 1, replace = true)
    }

    /** 上拉加载更多 */
    fun loadMore() {
        if (uiState.isLoadingMore || !uiState.hasMore) return
        uiState = uiState.copy(isLoadingMore = true)
        loadStations(page = uiState.currentPage + 1, replace = false)
    }

    /** 搜索（回车键触发） */
    fun search(name: String) {
        uiState = uiState.copy(searchName = name, stations = emptyList(), currentPage = 1)
        loadStations(page = 1, replace = true, name = name.takeIf { it.isNotBlank() })
    }

    /** 重试 */
    fun retry() {
        loadStations(page = uiState.currentPage, replace = true)
    }

    // ─── ViewModelFactory（无需 Hilt） ────────────────────────────
    companion object {
        fun factory(repository: StationRepository): ViewModelProvider.Factory =
            object : ViewModelProvider.Factory {
                @Suppress("UNCHECKED_CAST")
                override fun <T : ViewModel> create(modelClass: Class<T>): T =
                    HomeViewModel(repository) as T
            }
    }
}
