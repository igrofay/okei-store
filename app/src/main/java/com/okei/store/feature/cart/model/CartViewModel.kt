package com.okei.store.feature.cart.model

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.okei.store.domain.interactor.cart.CartInteractor
import com.okei.store.domain.model.cart.CartWithData
import com.okei.store.feature.common.model.Box
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CartViewModel @Inject constructor(
    private val cartInteractor: CartInteractor,
): ViewModel() {

    private val _cart = mutableStateOf(CartWithData(setOf(),0))
    val cart : State<CartWithData> = _cart

    private val _displayProductInformation = Channel<Box<String>>()
    val displayProductInformation : Flow<Box<String>>
        get() = _displayProductInformation.receiveAsFlow()

    private val subscription = cartInteractor.subscribeCartWithData{ cartWithData->
        _cart.value = cartWithData
    }

    fun clearCart(){
        cartInteractor.clear()
    }
    override fun onCleared() {
        super.onCleared()
        subscription.cancel()
        cartInteractor.cansel()
    }

    fun showProductModel(id: String){
        viewModelScope.launch {
            _displayProductInformation.send(Box(id))
        }
    }

    fun addProductInCart(id: String, quantity: Int = 1){
        cartInteractor.addProductInCart(id, quantity)
    }

    fun removeProductInCart(id: String, quantity: Int = 1){
        cartInteractor.removeProductInCart(id, quantity)
    }
}