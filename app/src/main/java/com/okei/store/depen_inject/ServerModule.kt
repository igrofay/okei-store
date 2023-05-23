package com.okei.store.depen_inject

import android.util.Log
import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import com.okei.store.data.data_source.api.server.AuthApi
import com.okei.store.data.data_source.api.server.ProductApi
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.serialization.json.Json
import okhttp3.Interceptor
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object ServerModule {
    private const val ServerUrl = "https://3f1a-95-183-16-18.ngrok-free.app"
    private val contentType = "application/json".toMediaType()

    @Provides
    @Singleton
    fun provideInterceptor(): Interceptor = Interceptor { chain ->
        val request = chain.request()
        Log.d("Interceptor", request.url.toString())
        val response = chain.proceed(request)
        Log.d("Interceptor", response.request.url.toString() +" => "+ response.code.toString())
        response
    }
    @Provides
    @Singleton
    fun provideClient(
        interceptor: Interceptor
    ) : OkHttpClient = OkHttpClient.Builder()
        .addInterceptor(interceptor)
        .build()
    @Provides
    @Singleton
    fun provideRetrofit(
        client: OkHttpClient,
    ) : Retrofit = Retrofit.Builder()
        .baseUrl(ServerUrl)
        .client(client)
        .addConverterFactory(Json.asConverterFactory(contentType))
        .build()
    @Provides
    fun provideAuthApi(
        retrofit: Retrofit
    ): AuthApi = retrofit
        .create(AuthApi::class.java)
    @Provides
    fun provideProductApi(
        retrofit: Retrofit
    ): ProductApi = retrofit
        .create(ProductApi::class.java)
}