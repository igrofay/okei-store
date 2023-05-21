package com.okei.store.feature.shop.model

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
import kotlinx.coroutines.Job
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ShopViewModel @Inject constructor(
    private val productRepos : ProductRepository,
    private val cartRepository: CartRepository,
) : ViewModel(){
    private val _listProduct = mutableStateListOf<ProductModel>()
    val listProduct : List<ProductModel> = _listProduct

    private val _foundProducts = mutableStateListOf<ProductModel>()
    val foundProducts: List<ProductModel> = _foundProducts

    private val _isRefreshing = mutableStateOf(true)
    val isRefreshing: State<Boolean> = _isRefreshing
    private val _isSearching = mutableStateOf(false)
    val isSearching: State<Boolean> = _isSearching

    private val _query = mutableStateOf("Поиск товаров")
    val query: State<String> = _query

    private val _displayProductInformation = Channel<Box<ProductModel>>()
    val displayProductInformation : Flow<Box<ProductModel>>
        get() = _displayProductInformation.receiveAsFlow()
    init {
        load()
    }

    private val _cart = mutableStateMapOf<String, Int>()
    val cart: Map<String, Int> = _cart

    private val subscriptionCart = viewModelScope.launch {
        cartRepository.getCart().collect{
            _cart.clear()
            _cart.putAll(it)
        }
    }
    private fun load(){
        viewModelScope.launch {
            val list = productRepos.getProducts()
            if(listProduct != list){
                _listProduct.clear()
                _listProduct.addAll(list)
            }
            _isRefreshing.value = false
        }
    }

    fun refresh() {
        _isRefreshing.value = true
        load()
    }

    fun cancelSearch() {
        _isSearching.value = false
        _query.value = "Поиск товаров"
        _foundProducts.clear()
    }

    fun openSearch() {
        _query.value = ""
        _isSearching.value = true

    }
    private var searchJob: Job? = null

    fun inputQuery(text: String) {
        _query.value = text
        if (text.isBlank()){
            _foundProducts.clear()
        }else{
            searchJob?.cancel()
            searchJob = viewModelScope.launch {
                delay(500)
                _foundProducts.clear()
                val list = productRepos.getProducts().filter {
                    it.name.contains(text) or it.description.contains(text)
                }
                _foundProducts.addAll(list)
            }
        }
    }
    fun search(text: String){
        searchJob?.cancel()
        searchJob = viewModelScope.launch{
            _foundProducts.clear()
            val list = productRepos.getProducts().filter {
                it.name.contains(text) or it.description.contains(text)
            }
            _foundProducts.addAll(list)
        }
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

    override fun onCleared() {
        super.onCleared()
        subscriptionCart.cancel()
    }
}