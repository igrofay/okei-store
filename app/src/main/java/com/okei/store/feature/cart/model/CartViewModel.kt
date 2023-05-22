package com.okei.store.feature.cart.model

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.okei.store.domain.iterator.cart.CartIterator
import com.okei.store.domain.model.cart.CartWithData
import com.okei.store.domain.model.product.ProductModel
import com.okei.store.domain.repos.CartRepository
import com.okei.store.domain.repos.ProductRepository
import com.okei.store.feature.common.model.Box
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CartViewModel @Inject constructor(
    private val cartIterator: CartIterator,
): ViewModel() {

    private val _cart = mutableStateOf(CartWithData(setOf(),0))
    val cart : State<CartWithData> = _cart

    private val _displayProductInformation = Channel<Box<ProductModel>>()
    val displayProductInformation : Flow<Box<ProductModel>>
        get() = _displayProductInformation.receiveAsFlow()

    private val subscription = viewModelScope.launch {
        cartIterator.subscribe().collect{ cartWithData->
            _cart.value = cartWithData
        }
    }

    fun clearCart(){
        cartIterator.clear()
    }
    override fun onCleared() {
        super.onCleared()
        subscription.cancel()
        cartIterator.cansel()
    }

    fun showProductModel(productModel: ProductModel){
        viewModelScope.launch {
            _displayProductInformation.send(Box(productModel))
        }
    }

    fun addProductInCart(id: String, quantity: Int = 1){
        cartIterator.addProductInCart(id, quantity)
    }

    fun removeProductInCart(id: String, quantity: Int = 1){
        cartIterator.removeProductInCart(id, quantity)
    }
}