package com.okei.store.data.data_source.api.server

import com.okei.store.data.model.CreateOrderBody
import com.okei.store.data.model.OrderBody
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST

interface OrderApi {
    @POST("/api/order")
    suspend fun createOrder(
        @Body createOrderBody: CreateOrderBody,
        @Header("Authorization") token: String
    )

    @GET("/api/orders")
    suspend fun getOrders(
        @Header("Authorization") token: String
    ) : List<OrderBody>
}