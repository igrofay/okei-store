package com.okei.store.feature.shop.view

import android.annotation.SuppressLint
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.hilt.navigation.compose.hiltViewModel
import com.okei.store.feature.app.view.collectSideEffect
import com.okei.store.feature.common.product.ProductInformationView
import com.okei.store.feature.nav.view.SheetContent
import com.okei.store.feature.shop.model.ShopSideEffect
import com.okei.store.feature.shop.model.ShopViewModel

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun ShopScreen(
    viewModel: ShopViewModel = hiltViewModel(),
    showBottomSheet: (SheetContent) -> Unit,
    hideBottomSheet: ()-> Unit,
) {
    LaunchedEffect(isSystemInDarkTheme() ){
        hideBottomSheet.invoke()
    }
    LaunchedEffect(viewModel){
        viewModel.refresh()
    }
    val snackbarHostState = remember { SnackbarHostState() }
    val res = LocalContext.current.resources
    viewModel.sideEffect.collectSideEffect{ sideEffect ->
        when(sideEffect){
            is ShopSideEffect.Message -> snackbarHostState
                .showSnackbar(res.getString(sideEffect.stringRes))
            is ShopSideEffect.ProductInformation -> showBottomSheet.invoke {
                val product = remember(viewModel.listProduct) {
                    viewModel.getProduct(sideEffect.id)
                } ?: return@invoke hideBottomSheet.invoke()
                ProductInformationView(
                    productModel = product,
                    isAddedToCart = viewModel.cart.contains(sideEffect.id),
                    quantityInCart = viewModel.cart.getOrDefault(sideEffect.id, 0),
                    minus = { viewModel.removeProductInCart(sideEffect.id) },
                    plus = { viewModel.addProductInCart(sideEffect.id) }
                )
            }
        }
    }
    Scaffold(
        snackbarHost ={
            SnackbarHost(snackbarHostState)
        }
    ) {
        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            SearchProductView(viewModel)
            ListProductView(viewModel)
        }
    }
}