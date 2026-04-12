package com.milok.app.di

import com.milok.app.data.repository.PostRepository
import com.milok.app.data.repository.PostRepositoryImpl
import com.milok.app.data.repository.StationRepository
import com.milok.app.data.repository.StationRepositoryImpl
import com.milok.app.data.repository.UserRepository
import com.milok.app.data.repository.UserRepositoryImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {

    @Binds
    @Singleton
    abstract fun bindPostRepository(
        impl: PostRepositoryImpl
    ): PostRepository

    @Binds
    @Singleton
    abstract fun bindUserRepository(
        impl: UserRepositoryImpl
    ): UserRepository

    @Binds
    @Singleton
    abstract fun bindStationRepository(
        impl: StationRepositoryImpl
    ): StationRepository
}
