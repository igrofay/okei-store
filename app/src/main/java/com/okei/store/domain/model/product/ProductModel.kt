package com.okei.store.domain.model.product

interface ProductModel {
    val id: String
    val name: String
    val mainImageUrl: String
    val listImageUrl: List<String>
    val description: String
    val price: Int
    val quantityInStock: Int
}