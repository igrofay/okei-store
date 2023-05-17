package com.okei.store.domain.repos

import kotlinx.coroutines.flow.Flow

interface CartRepository {
    // String is id product
    // int is num product
    fun getCart() : Flow<Map<String, Int>>
    suspend fun add(id: String)
    suspend fun remove(id: String)
    suspend fun changeNum(id: String, num: Int)
    suspend fun clear()
}