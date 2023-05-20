package com.okei.store.data.data_source.api.server

import com.okei.store.data.model.AuthBody
import com.okei.store.data.model.TokenBody
import retrofit2.http.Body
import retrofit2.http.POST

interface AuthApi {
    @POST("/signin")
    suspend fun sign(@Body authBody: AuthBody) : TokenBody
}