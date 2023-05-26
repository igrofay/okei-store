package com.okei.store.domain.use_case.order

import android.util.Log
import com.okei.store.domain.model.order.CreateOrderModel
import com.okei.store.domain.model.order.PaymentType
import com.okei.store.domain.repos.AppRepository
import com.okei.store.domain.repos.CartRepository
import com.okei.store.domain.repos.OrderRepository
import kotlinx.coroutines.flow.first
import java.util.UUID
import javax.inject.Inject

class CreateOrderUseCase @Inject constructor(
    private val cartRepository: CartRepository,
    private val appRepository: AppRepository,
    private val orderRepository: OrderRepository,
) {
    suspend fun execute(paymentType: PaymentType) = runCatching {
        val cart = cartRepository.getCart().first()
        val token =  appRepository.getAccessToken()!!
        orderRepository.createOrder(
            object : CreateOrderModel{
                override val id = UUID.randomUUID().toString()
                override val productQuantity = cart
                override val paymentType = paymentType
            },
            token
        )
        cartRepository.clear()
    }
}