package com.okei.store.domain.repos

import com.okei.store.domain.model.order.CreateOrderModel

interface OrderRepository {

    suspend fun createOrder(createOrderModel: CreateOrderModel, token: String)
}