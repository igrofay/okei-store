package com.okei.store.data.repos

import android.util.Log
import com.okei.store.data.data_source.api.server.OrderApi
import com.okei.store.data.model.CreateOrderBody.Companion.fromModelToCreateOrderBody
import com.okei.store.domain.model.error.AppError
import com.okei.store.domain.model.error.OrderError
import com.okei.store.domain.model.order.CreateOrderModel
import com.okei.store.domain.repos.OrderRepository
import retrofit2.HttpException
import java.io.IOException
import javax.inject.Inject

class OrderRepositoryImpl @Inject constructor(
    private val orderApi: OrderApi,
): OrderRepository {
    override suspend fun createOrder(createOrderModel: CreateOrderModel, token: String) {
        try {
            Log.e("OrderRepositoryImpl", "1")
            orderApi.createOrder(createOrderModel.fromModelToCreateOrderBody(), "Bearer $token")
            Log.e("OrderRepositoryImpl", "2")
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
}