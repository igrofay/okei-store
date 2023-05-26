package com.okei.store.domain.model.order

interface OrderModel {
    val id: String
    val code: String
    val paymentType: PaymentType
    val date: String
    val sum: Int
    val product: Map<String, String> // Имя и количество умноженное на стоимость
    // Глобус - 2 x 500 руб.
    // Дискета - 4 x 200 руб.
}