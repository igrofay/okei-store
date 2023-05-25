package com.okei.store.feature.cart.view

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.okei.store.R
import com.okei.store.domain.model.cart.ProductQuantity
import com.okei.store.feature.app.view.collectSideEffect
import com.okei.store.feature.cart.model.CartViewModel
import com.okei.store.feature.common.view.product.ProductInformationView
import com.okei.store.feature.nav.view.SheetContent


@Composable
fun CartScreen(
    viewModel: CartViewModel = hiltViewModel(),
    createOrder: ()->Unit,
    showBottomSheet: (SheetContent) -> Unit,
    hideBottomSheet: ()-> Unit,
) {
    LaunchedEffect(isSystemInDarkTheme() ){
        hideBottomSheet.invoke()
    }
    val cart by viewModel.cart
    viewModel.displayProductInformation.collectSideEffect{box ->
        showBottomSheet.invoke {
            val productQuantity = remember(cart){
                cart.get(box.value)
            } ?: return@invoke hideBottomSheet.invoke()
            ProductInformationView(
                productModel= productQuantity.product,
                isAddedToCart = cart.contains(box.value),
                quantityInCart = productQuantity.amount,
                minus = { viewModel.removeProductInCart(productQuantity.product.id) },
                plus = { viewModel.addProductInCart(productQuantity.product.id) }
            )
        }
    }
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(top = 32.dp)
    ) {
        Row(
            Modifier
                .padding(bottom = 12.dp)
                .fillMaxWidth()
                .padding(horizontal = 20.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Text(
                text = stringResource(id = R.string.cart),
                fontSize = 24.sp,
                fontWeight = FontWeight.W700,
            )
            IconButton(onClick = { viewModel.clearCart() }) {
                Icon(imageVector = Icons.Default.Delete, contentDescription = null)
            }
        }
        LazyColumn(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f),
            contentPadding = PaddingValues(vertical = 14.dp)
        ) {
            items(cart.setProductQuantity.toList()){ productQuantity->
                ItemCartView(
                    productModel =  productQuantity.product,
                    isAddedToCart = cart.contains(productQuantity.product.id),
                    quantityInCart =  productQuantity.amount,
                    minus = { viewModel.removeProductInCart(productQuantity.product.id) },
                    plus = { viewModel.addProductInCart(productQuantity.product.id) }
                ) {
                    viewModel.showProductModel(productQuantity.product.id)
                }
                Divider()
            }
        }
        Row(
            Modifier
                .padding(horizontal = 20.dp)
                .padding(top = 12.dp)
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
        ) {
            Text(
                text = stringResource(id = R.string.sum),
                fontSize = 20.sp,
                fontWeight = FontWeight.W600,
            )
            Text(
                text ="${cart.sum} ₽",
                fontSize = 20.sp,
                fontWeight = FontWeight.W600,
            )
        }
        Button(
            onClick = createOrder,
            modifier = Modifier
                .padding(horizontal = 20.dp, vertical = 24.dp)
                .fillMaxWidth(),
            enabled = cart.sum >= 0
        ) {
            Text(
                text = stringResource(id = R.string.proceed_to_checkout),
                fontSize = 16.sp
            )
        }
    }
}