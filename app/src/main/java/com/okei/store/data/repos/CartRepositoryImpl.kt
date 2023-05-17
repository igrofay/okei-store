package com.okei.store.data.repos

import androidx.datastore.core.DataStore
import com.okei.store.depen_inject.CartDataStore
import com.okei.store.domain.repos.CartRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class CartRepositoryImpl @Inject constructor(
    @CartDataStore
    private val cartDataStore: DataStore<Map<String, Int>>
) : CartRepository {
    override fun getCart(): Flow<Map<String, Int>> = cartDataStore.data
    override suspend fun add(id: String) {
        cartDataStore.updateData {
            it.toMutableMap().apply {
                this[id] = 1
            }
        }
    }

    override suspend fun remove(id: String) {
        cartDataStore.updateData {
            it.toMutableMap().apply {
                remove(id)
            }
        }
    }

    override suspend fun changeNum(id: String, num: Int) {
        cartDataStore.updateData {
            it.toMutableMap().apply {
                this[id] = num
            }
        }
    }

    override suspend fun clear() {
        cartDataStore.updateData { mapOf() }
    }
}