package com.okei.store.data.data_source.api.server

import com.okei.store.data.model.CreateOrderBody
import retrofit2.http.Body
import retrofit2.http.Header
import retrofit2.http.POST

interface OrderApi {
    @POST("/api/order")
    suspend fun createOrder(
        @Body createOrderBody: CreateOrderBody,
        @Header("Authorization") token: String
    )
}