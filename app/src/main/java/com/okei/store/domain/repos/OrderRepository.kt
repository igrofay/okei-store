package com.okei.store.domain.repos

import com.okei.store.domain.model.order.CreateOrderModel
import com.okei.store.domain.model.order.OrderModel

interface OrderRepository {

    suspend fun createOrder(createOrderModel: CreateOrderModel, token: String)

    suspend fun getOrdersUser(token: String) : List<OrderModel>
}