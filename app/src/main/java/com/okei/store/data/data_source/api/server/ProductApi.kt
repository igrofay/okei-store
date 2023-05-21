package com.okei.store.data.data_source.api.server

import com.okei.store.data.model.ProductBody
import retrofit2.http.GET

interface ProductApi {
    @GET("/api/products")
    suspend fun getProducts() : List<ProductBody>
}