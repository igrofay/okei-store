package com.okei.store.feature.shop.model

import androidx.annotation.StringRes
import com.okei.store.domain.model.product.ProductModel

sealed class ShopSideEffect {
    class Message(@StringRes val stringRes: Int) : ShopSideEffect()
    class ProductInformation(val id: String) : ShopSideEffect()
}