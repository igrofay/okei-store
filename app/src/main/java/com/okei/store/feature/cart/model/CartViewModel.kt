package com.okei.store.feature.cart.model

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.okei.store.domain.model.product.ProductModel
import com.okei.store.domain.repos.CartRepos
import com.okei.store.domain.repos.ProductRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CartViewModel @Inject constructor(
    private val cartRepos: CartRepos,
    private val productRepository: ProductRepository,
): ViewModel() {

    private val _listProduct = mutableStateListOf<ProductModel>()
    val listProduct : List<ProductModel> = _listProduct

    private val _cart = mutableStateMapOf<String, Int>()
    val cart: Map<String, Int> = _cart

    private val _sum = mutableStateOf(0)
    val sum : State<Int> = _sum

    private val _displayProductInformation = mutableStateOf<ProductModel?>(null)
    val displayProductInformation : State<ProductModel?> = _displayProductInformation

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
        cartRepos.getCart().collect{map ->
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
            cartRepos.clear()
        }
    }
    override fun onCleared() {
        super.onCleared()
        subscriptionCart.cancel()
        subscriptionCatalog.cancel()
    }

    fun showProductModel(productModel: ProductModel){
        _displayProductInformation.value = productModel
    }
    fun closeProductModel(){
        _displayProductInformation.value = null
    }

    fun addProductInCart(id: String, quantity: Int = 1){
        viewModelScope.launch {
            if (cart.contains(id)){
                cartRepos.changeNum(id, cart.getOrDefault(id, 0) + quantity)
            }else{
                cartRepos.add(id)
            }
        }
    }

    fun removeProductInCart(id: String, quantity: Int = 1){
        viewModelScope.launch {
            val value = cart.getOrDefault(id, 0) - quantity
            if (value == 0) {
                cartRepos.remove(id)
            }else if(value>0){
                cartRepos.changeNum(id, value)
            }else{
                if (cart.contains(id))
                    cartRepos.remove(id)
            }
        }
    }
}