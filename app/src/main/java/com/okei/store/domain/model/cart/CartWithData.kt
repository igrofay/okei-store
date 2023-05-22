package com.okei.store.domain.model.cart

data class CartWithData(
    val setProductQuantity : Set<ProductQuantity>,
    val sum: Int,
){
    fun contains(id: String) : Boolean {
        for (item in setProductQuantity){
            if(item.product.id == id)
                return true
        }
        return false
    }

    fun get(id: String): ProductQuantity? {
        for (item in setProductQuantity){
            if (item.product.id == id)
                return item
        }
        return null
    }

}