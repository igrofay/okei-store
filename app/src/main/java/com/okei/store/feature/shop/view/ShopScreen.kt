package com.okei.store.feature.shop.view

import androidx.compose.foundation.layout.*
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.SheetValue
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import com.okei.store.feature.common.product.ProductInformationView
import com.okei.store.feature.nav.view.LocalBottomSheetManager
import com.okei.store.feature.shop.model.ShopViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ShopScreen(
    viewModel: ShopViewModel = hiltViewModel()
) {
    val bottomSheetManager = LocalBottomSheetManager.current
    val productModel by viewModel.displayProductInformation

    bottomSheetManager.collectAsState {
        if (it == SheetValue.Hidden) {
            viewModel.closeProductModel()
            bottomSheetManager.onClose()
        }
    }
    LaunchedEffect(productModel) {
        productModel?.let { productModel ->
            bottomSheetManager.show {
                ProductInformationView(productModel,
                    isAddedToCart = viewModel.cart.contains(productModel.id),
                    quantityInCart = viewModel.cart.getOrDefault(productModel.id, 0),
                    minus = { viewModel.removeProductInCart(productModel.id) },
                    plus = { viewModel.addProductInCart(productModel.id) }
                )
            }
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