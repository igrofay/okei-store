package com.okei.store.domain.iterator.cart

import com.okei.store.domain.model.cart.CartWithData
import com.okei.store.domain.model.cart.ProductQuantity
import com.okei.store.domain.model.product.ProductModel
import com.okei.store.domain.repos.CartRepository
import com.okei.store.domain.repos.ProductRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.cancel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.launch
import javax.inject.Inject

class CartIterator @Inject constructor(
    private val cartRepository: CartRepository,
    private val productRepository: ProductRepository,
) {
    private val coroutine = CoroutineScope(Dispatchers.Default)
    private val flowProducts = flow<List<ProductModel>> {
        while (true){
            runCatching {
                productRepository.getProducts()
            }.onSuccess {products->
                catalog.clear()
                catalog.addAll(products)
            }
            delay(5_000)
        }
    }


    private val cart: MutableMap<String, Int> = mutableMapOf()
    private val catalog: MutableList<ProductModel> = mutableListOf()


    fun subscribe() = combine(cartRepository.getCart(), flowProducts, ::transformation)

    private fun transformation(
        map: Map<String, Int>,
        list: List<ProductModel>
    ) : CartWithData{
        cart.clear()
        cart.putAll(map)
        catalog.clear()
        catalog.addAll(list)
        val listProductQuantity = catalog
            .filter { productModel -> cart.contains(productModel.id) }
            .map { product->
                ProductQuantity(
                    amount = cart.getValue(product.id),
                    product = product
                )
            }.toSet()
        return CartWithData(
            setProductQuantity = listProductQuantity,
            sum = listProductQuantity.sumOf { it.sum() }
        )
    }

    fun cansel() = coroutine.cancel()

    fun addProductInCart(id: String, quantity: Int){
        coroutine.launch {
            if (cart.contains(id)){
                cartRepository.changeNum(id, cart.getOrDefault(id, 0) + quantity)
            }else{
                cartRepository.add(id)
            }
        }
    }
    fun removeProductInCart(id: String, quantity: Int){
        coroutine.launch {
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
    fun clear(){
        coroutine.launch {
            cartRepository.clear()
        }
    }
}