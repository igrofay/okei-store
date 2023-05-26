package com.okei.store.feature.cart.model

import androidx.annotation.StringRes
import com.okei.store.domain.model.product.ProductModel

sealed class CartSideEffect {
    class ProductInformation(val id: String) : CartSideEffect()
    class Message(@StringRes val stringRes: Int) : CartSideEffect()
}