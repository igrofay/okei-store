package com.okei.store.feature.cart.model

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
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
    private val cartRepository: CartRepository,
    private val productRepository: ProductRepository,
): ViewModel() {

    private val _listProduct = mutableStateListOf<ProductModel>()
    val listProduct : List<ProductModel> = _listProduct

    private val _cart = mutableStateMapOf<String, Int>()
    val cart: Map<String, Int> = _cart

    private val _sum = mutableStateOf(0)
    val sum : State<Int> = _sum

    private val _displayProductInformation = Channel<Box<ProductModel>>()
    val displayProductInformation : Flow<Box<ProductModel>>
        get() = _displayProductInformation.receiveAsFlow()

    private val catalog = mutableStateListOf<ProductModel>()

    private val subscriptionCatalog = viewModelScope.launch {
        while (true){
            val products = productRepository.getProducts()
            catalog.clear()
            catalog.addAll(products)
            val list = catalog.filter { productModel -> _cart.contains(productModel.id) }
            _listProduct.clear()
            _listProduct.addAll(list)
            _sum.value = list.sumOf { it.price * _cart.getValue(it.id) }
            delay(5_000)
        }
    }

    private val subscriptionCart = viewModelScope.launch {
        cartRepository.getCart().collect{ map ->
            _cart.clear()
            _cart.putAll(map)
            val list = catalog.filter { productModel -> _cart.contains(productModel.id) }
            _listProduct.clear()
            _listProduct.addAll(list)
            _sum.value = list.sumOf { it.price * _cart.getValue(it.id) }
        }
    }

    fun clearCart(){
        viewModelScope.launch {
            cartRepository.clear()
        }
    }
    override fun onCleared() {
        super.onCleared()
        subscriptionCart.cancel()
        subscriptionCatalog.cancel()
    }

    fun showProductModel(productModel: ProductModel){
        viewModelScope.launch {
            _displayProductInformation.send(Box(productModel))
        }
    }

    fun addProductInCart(id: String, quantity: Int = 1){
        viewModelScope.launch {
            if (cart.contains(id)){
                cartRepository.changeNum(id, cart.getOrDefault(id, 0) + quantity)
            }else{
                cartRepository.add(id)
            }
        }
    }

    fun removeProductInCart(id: String, quantity: Int = 1){
        viewModelScope.launch {
            val value = cart.getOrDefault(id, 0) - quantity
            if (value == 0) {
                cartRepository.remove(id)
            }else if(value>0){
                cartRepository.changeNum(id, value)
            }else{
                if (cart.contains(id))
                    cartRepository.remove(id)
            }
        }
    }
}