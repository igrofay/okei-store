package com.okei.store.feature.cart.view

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.ViewModel
import com.okei.store.R
import com.okei.store.feature.cart.model.CartViewModel
import com.okei.store.feature.common.product.ProductInformationView
import com.okei.store.feature.nav.view.LocalBottomSheetManager

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CartScreen(
    viewModel: CartViewModel = hiltViewModel(),
    createOrder: ()->Unit,
) {
    val bottomSheetManager = LocalBottomSheetManager.current
    val displayProductInformation by viewModel.displayProductInformation
    bottomSheetManager.collectAsState {
        if (it == SheetValue.Hidden) {
            viewModel.closeProductModel()
            bottomSheetManager.onClose()
        }
    }
    LaunchedEffect(displayProductInformation){
        displayProductInformation?.let { productModel->
            bottomSheetManager.show {
                ProductInformationView(
                    productModel = productModel,
                    isAddedToCart = viewModel.cart.contains(productModel.id),
                    quantityInCart = viewModel.cart.getOrDefault(productModel.id, 0),
                    minus = { viewModel.removeProductInCart(productModel.id) },
                    plus = { viewModel.addProductInCart(productModel.id) }
                )
            }
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
            items(viewModel.listProduct){
                ItemCartView(
                    productModel = it,
                    isAddedToCart = viewModel.cart.contains(it.id),
                    quantityInCart = viewModel.cart.getOrDefault(it.id, 0),
                    minus = { viewModel.removeProductInCart(it.id) },
                    plus = { viewModel.addProductInCart(it.id) }
                ) {
                    viewModel.showProductModel(it)
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
                text ="${viewModel.sum.value} ₽",
                fontSize = 20.sp,
                fontWeight = FontWeight.W600,
            )
        }
        Button(
            onClick = createOrder,
            modifier = Modifier
                .padding(horizontal = 20.dp, vertical = 24.dp)
                .fillMaxWidth(),
            enabled = viewModel.sum.value > 0
        ) {
            Text(
                text = stringResource(id = R.string.proceed_to_checkout),
                fontSize = 16.sp
            )
        }
    }
}