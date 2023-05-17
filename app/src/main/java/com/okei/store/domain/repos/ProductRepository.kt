package com.okei.store.domain.repos

import com.okei.store.domain.model.product.ProductModel

interface ProductRepository {
    suspend fun getProducts() : List<ProductModel>
}