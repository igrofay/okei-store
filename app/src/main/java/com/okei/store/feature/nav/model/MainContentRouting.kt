package com.okei.store.feature.nav.model

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import com.okei.store.R

internal sealed class MainContentRouting(
    val route: String,
    @DrawableRes val icon: Int,
    @StringRes val header: Int,
) {
    object ProductList : MainContentRouting(
        "product_list",
        R.drawable.ic_store,
        R.string.store
    )

    object Cart : MainContentRouting(
        "cart",
        R.drawable.ic_cart,
        R.string.cart
    )

    object Profile : MainContentRouting(
        "profile",
        R.drawable.ic_profile,
        R.string.profile
    )

    companion object {
        val listRouting = listOf(ProductList, Cart, Profile)
    }
}