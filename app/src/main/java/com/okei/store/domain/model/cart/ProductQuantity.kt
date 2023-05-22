package com.okei.store.domain.model.cart

import com.okei.store.domain.model.product.ProductModel

class ProductQuantity(
    val amount: Int,
    val product: ProductModel,
){
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as ProductQuantity
        return product.id == other.product.id
    }

    override fun hashCode(): Int {
        return product.id.hashCode()
    }
    fun sum() = amount * product.price
}
