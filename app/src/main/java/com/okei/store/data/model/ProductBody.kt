package com.okei.store.data.model

import com.okei.store.domain.model.product.ProductModel
import kotlinx.serialization.Serializable

@Serializable
data class ProductBody(
    override val id: String,
    override val name: String,
    override val mainImageUrl: String,
    override val listImageUrl: List<String>,
    override val description: String,
    override val price: Int,
    override val quantityInStock: Int
) : ProductModel