package com.okei.store.data.model

import com.okei.store.domain.model.order.OrderModel
import com.okei.store.domain.model.order.PaymentType
import kotlinx.serialization.Serializable

@Serializable
data class OrderBody(
    override val id: String,
    override val code: String,
    override val paymentType: PaymentType,
    override val date: String,
    override val sum: Int,
    override val product: Map<String, String>
) : OrderModel