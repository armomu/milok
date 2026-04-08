package com.milok.app.di

import com.milok.app.BuildConfig
import com.milok.app.data.network.AuthInterceptor
import com.milok.app.data.network.CommonHeaderInterceptor
import com.milok.app.data.network.MilokApiService
import com.milok.app.data.network.ResponseCodeInterceptor
import com.milok.app.data.store.UserPreferencesStore
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.runBlocking
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {

    private const val CONNECT_TIMEOUT = 15L
    private const val READ_TIMEOUT = 30L
    private const val WRITE_TIMEOUT = 30L

    @Provides
    @Singleton
    fun provideLoggingInterceptor(): HttpLoggingInterceptor =
        HttpLoggingInterceptor().apply {
            level = if (BuildConfig.DEBUG) {
                HttpLoggingInterceptor.Level.BODY
            } else {
                HttpLoggingInterceptor.Level.NONE
            }
        }

    @Provides
    @Singleton
    fun provideAuthInterceptor(
        preferencesStore: UserPreferencesStore
    ): AuthInterceptor = AuthInterceptor {
        // 同步读取 token（仅在拦截器中使用）
        runBlocking { preferencesStore.userToken.firstOrNull() }
    }

    @Provides
    @Singleton
    fun provideCommonHeaderInterceptor(): CommonHeaderInterceptor =
        CommonHeaderInterceptor(appVersion = BuildConfig.VERSION_NAME)

    @Provides
    @Singleton
    fun provideResponseCodeInterceptor(): ResponseCodeInterceptor =
        ResponseCodeInterceptor()

    @Provides
    @Singleton
    fun provideOkHttpClient(
        loggingInterceptor: HttpLoggingInterceptor,
        authInterceptor: AuthInterceptor,
        commonHeaderInterceptor: CommonHeaderInterceptor,
        responseCodeInterceptor: ResponseCodeInterceptor
    ): OkHttpClient = OkHttpClient.Builder()
        .connectTimeout(CONNECT_TIMEOUT, TimeUnit.SECONDS)
        .readTimeout(READ_TIMEOUT, TimeUnit.SECONDS)
        .writeTimeout(WRITE_TIMEOUT, TimeUnit.SECONDS)
        .addInterceptor(commonHeaderInterceptor)
        .addInterceptor(authInterceptor)
        .addInterceptor(responseCodeInterceptor)
        .addInterceptor(loggingInterceptor) // 放最后，记录完整请求
        .build()

    @Provides
    @Singleton
    fun provideRetrofit(okHttpClient: OkHttpClient): Retrofit =
        Retrofit.Builder()
            .baseUrl(BuildConfig.BASE_URL)
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()

    @Provides
    @Singleton
    fun provideMilokApiService(retrofit: Retrofit): MilokApiService =
        retrofit.create(MilokApiService::class.java)
}
