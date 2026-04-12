package com.milok.app.di

import com.milok.app.data.repository.StationRepository
import dagger.hilt.EntryPoint
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

/**
 * Hilt EntryPoint，用于在非 Hilt-managed 组件（如手动创建 ViewModel）中获取依赖
 */
@EntryPoint
@InstallIn(SingletonComponent::class)
interface HomeViewModelEntryPoint {
    fun stationRepository(): StationRepository
}
