package com.okei.store.depen_inject

import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import com.okei.store.data.data_source.api.server.AuthApi
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import retrofit2.Retrofit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object ServerModule {
    private const val ServerUrl = "http://192.168.101.91/"
    private val contentType = "application/json".toMediaType()

    @Provides
    @Singleton
    fun provideRetrofit() : Retrofit = Retrofit.Builder()
        .baseUrl(ServerUrl)
        .addConverterFactory(Json.asConverterFactory(contentType))
        .build()
    @Provides
    fun provideAuthApi(
        retrofit: Retrofit
    ): AuthApi = retrofit
        .create(AuthApi::class.java)
}