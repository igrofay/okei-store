package com.okei.store.domain.use_case.order

import com.okei.store.domain.repos.AppRepository
import com.okei.store.domain.repos.OrderRepository
import javax.inject.Inject

class GetOrdersUseCase @Inject constructor(
    private val appRepository: AppRepository,
    private val orderRepository: OrderRepository,
) {
    suspend fun execute() = runCatching {
        orderRepository.getOrdersUser(
            appRepository.getAccessToken()!!
        )
    }
}