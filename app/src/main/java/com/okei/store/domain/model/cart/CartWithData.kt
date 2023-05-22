package com.okei.store.domain.model.cart

import com.okei.store.domain.model.product.ProductModel

data class CartWithData(
    val setProductQuantity : Set<ProductQuantity>,
    val sum: Int,
){
//    fun contains(product: ProductModel) = setProductQuantity.contains()
}