package com.okei.store.data.repos

import android.util.Log
import com.okei.store.data.data_source.api.server.OrderApi
import com.okei.store.data.model.CreateOrderBody.Companion.fromModelToCreateOrderBody
import com.okei.store.domain.model.error.AppError
import com.okei.store.domain.model.error.OrderError
import com.okei.store.domain.model.order.CreateOrderModel
import com.okei.store.domain.model.order.OrderModel
import com.okei.store.domain.repos.OrderRepository
import retrofit2.HttpException
import java.io.IOException
import javax.inject.Inject

class OrderRepositoryImpl @Inject constructor(
    private val orderApi: OrderApi,
): OrderRepository {
    override suspend fun createOrder(createOrderModel: CreateOrderModel, token: String) {
        try {
            orderApi.createOrder(createOrderModel.fromModelToCreateOrderBody(), "Bearer $token")
        }catch (e: IOException){
            throw AppError.NoNetworkAccess
        }catch (e: HttpException){
            when(e.code()){
                400 -> throw OrderError.RequiredProductDoesNotExistOrIsNotAvailableInThatQuantity
                409 -> throw OrderError.AnOrderWithThisIdExists
                else-> throw e
            }
        }
    }

    override suspend fun getOrdersUser(token: String): List<OrderModel> {
        try {
            return orderApi.getOrders("Bearer $token")
        }catch (e: IOException){
            throw AppError.NoNetworkAccess
        }
    }
}