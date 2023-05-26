package com.okei.store.data.model

import com.okei.store.domain.model.order.CreateOrderModel
import com.okei.store.domain.model.order.PaymentType
import kotlinx.serialization.Serializable

@Serializable
data class CreateOrderBody(
    override val id: String,
    override val productQuantity: Map<String, Int>,
    override val paymentType: PaymentType
) : CreateOrderModel{
    companion object{
        fun CreateOrderModel.fromModelToCreateOrderBody() = CreateOrderBody(
            id, productQuantity, paymentType
        )
    }
}