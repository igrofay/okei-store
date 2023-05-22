package com.okei.store.feature.shop.model

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.okei.store.R
import com.okei.store.domain.interactor.cart.CartInteractor
import com.okei.store.domain.model.error.AppError
import com.okei.store.domain.model.product.ProductModel
import com.okei.store.domain.repos.CartRepository
import com.okei.store.domain.use_case.product.GetProductUseCase
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
    private val getProductUseCase: GetProductUseCase,
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

    private val _sideEffect = Channel<ShopSideEffect>()
    val sideEffect : Flow<ShopSideEffect>
        get() = _sideEffect.receiveAsFlow()

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

    fun getProduct(id: String) : ProductModel?{
        for (item in listProduct){
            if (item.id == id)
                return item
        }
        return null
    }
    private fun load(){
        viewModelScope.launch {
            getProductUseCase.execute()
                .onSuccess { list->
                    if(listProduct != list){
                        _listProduct.clear()
                        _listProduct.addAll(list)
                    }
                }.onFailure {
                    when(it){
                        AppError.NoNetworkAccess -> _sideEffect.send(
                            ShopSideEffect.Message(R.string.lack_of_access_to_internet)
                        )
                    }
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
            search(text, 500L)
        }
    }
    fun search(text: String, delayMilliseconds: Long = 0){
        searchJob?.cancel()
        searchJob = viewModelScope.launch {
            delay(delayMilliseconds)
            _foundProducts.clear()
            getProductUseCase.execute()
                .onSuccess {list->
                    val products = list.filter {
                        it.name.contains(text) or it.description.contains(text)
                    }
                    _foundProducts
                        .addAll(products)
                }.onFailure {
                    when(it){
                        AppError.NoNetworkAccess -> _sideEffect.send(
                            ShopSideEffect.Message(R.string.lack_of_access_to_internet)
                        )
                    }
                }
        }
    }
    fun showProductModel(id: String){
        viewModelScope.launch {
            _sideEffect.send(ShopSideEffect.ProductInformation(id))
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