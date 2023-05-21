package com.okei.store.feature.shop.view

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import com.okei.store.feature.app.view.collectSideEffect
import com.okei.store.feature.common.product.ProductInformationView
import com.okei.store.feature.nav.view.SheetContent
import com.okei.store.feature.shop.model.ShopViewModel

@Composable
fun ShopScreen(
    viewModel: ShopViewModel = hiltViewModel(),
    showBottomSheet: (SheetContent) -> Unit,
    hideBottomSheet: ()-> Unit,
) {
    LaunchedEffect(isSystemInDarkTheme() ){
        hideBottomSheet.invoke()
    }
    viewModel.displayProductInformation.collectSideEffect{ box ->
        val productModel = box.value
        showBottomSheet.invoke {
            ProductInformationView(productModel,
                isAddedToCart = viewModel.cart.contains(productModel.id),
                quantityInCart = viewModel.cart.getOrDefault(productModel.id, 0),
                minus = { viewModel.removeProductInCart(productModel.id) },
                plus = { viewModel.addProductInCart(productModel.id) }
            )
        }
    }
    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        SearchProductView(viewModel)
        ListProductView(viewModel)
    }
}