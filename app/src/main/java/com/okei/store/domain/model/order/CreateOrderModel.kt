package com.okei.store.domain.model.order

interface CreateOrderModel {
    val id: String
    val productQuantity: Map<String,Int>
    val paymentType: PaymentType
}