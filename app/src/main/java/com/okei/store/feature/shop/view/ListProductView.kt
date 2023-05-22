package com.okei.store.feature.shop.view

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.pullrefresh.PullRefreshIndicator
import androidx.compose.material.pullrefresh.pullRefresh
import androidx.compose.material.pullrefresh.rememberPullRefreshState
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.okei.store.R
import com.okei.store.domain.model.product.ProductModel
import com.okei.store.feature.shop.model.ShopViewModel

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun ListProductView(viewModel: ShopViewModel) {
    val refreshing by viewModel.isRefreshing
    val pullRefreshState = rememberPullRefreshState(refreshing, { viewModel.refresh() })
    val cart = viewModel.cart
    Box(
        modifier = Modifier
            .fillMaxSize()
            .pullRefresh(pullRefreshState)
    ) {

        LazyVerticalGrid(
            columns = GridCells.Adaptive(150.dp),
            modifier = Modifier
                .fillMaxSize(),
            contentPadding = PaddingValues(
                horizontal = 20.dp,
                vertical = 12.dp
            ),
            verticalArrangement = Arrangement.spacedBy(8.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
        ) {
            items(viewModel.listProduct) {
                ItemProduct(
                    productModel = it,
                    isAddedToCart = cart.contains(it.id),
                    quantityInCart = cart.getOrDefault(it.id, 0),
                    minus = {viewModel.removeProductInCart(it.id)},
                    plus = {viewModel.addProductInCart(it.id)}
                ) {
                    viewModel.showProductModel(it.id)
                }
            }
        }
        PullRefreshIndicator(
            refreshing,
            pullRefreshState,
            Modifier.align(Alignment.TopCenter),
            contentColor = MaterialTheme.colorScheme.primary
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ItemProduct(
    productModel: ProductModel,
    isAddedToCart: Boolean,
    quantityInCart: Int,
    minus: () -> Unit,
    plus: () -> Unit,
    onClick: () -> Unit
) {
    ElevatedCard(
        onClick = onClick,
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .height(280.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            AsyncImage(
                model = productModel.mainImageUrl,
                contentDescription = null,
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(150.dp),
            )
            Spacer(modifier = Modifier.weight(1f))
            Text(
                text = productModel.name,
                maxLines = 3,
                fontSize = 15.sp,
                fontWeight = FontWeight.Medium,
                textAlign = TextAlign.Center,
                overflow = TextOverflow.Ellipsis,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
                color = MaterialTheme.colorScheme.onSurface
            )
            Spacer(modifier = Modifier.weight(1f))

            Row(
                modifier = Modifier
                    .padding(bottom = 16.dp)
                    .padding(horizontal = 20.dp)
                    .fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                if (isAddedToCart){
                    FilledIconButton(
                        onClick = minus,
                        modifier = Modifier.size(28.dp),
                        shape = MaterialTheme.shapes.small,
                        enabled =  0 < quantityInCart,
                    ) {
                        Icon(painter = painterResource(id = R.drawable.ic_remove), contentDescription = null)
                    }

                    Text(
                        text = quantityInCart.toString(),
                        fontSize = 16.sp,
                        color = MaterialTheme.colorScheme.onSurface,
                        fontWeight = FontWeight.Medium
                    )
                    FilledIconButton(
                        onClick = plus,
                        modifier = Modifier.size(28.dp),
                        shape = MaterialTheme.shapes.small,
                        enabled = productModel.quantityInStock > quantityInCart
                    ) {
                        Icon(imageVector = Icons.Default.Add, contentDescription = null)
                    }
                } else{
                    Text(
                        text = "${productModel.price} ₽",
                        fontSize = 14.sp,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    FilledIconButton(
                        onClick = plus,
                        modifier = Modifier.size(28.dp),
                        shape = MaterialTheme.shapes.small,
                        enabled = productModel.quantityInStock > quantityInCart
                    ) {
                        Icon(imageVector = Icons.Default.Add, contentDescription = null)
                    }
                }
            }
        }
    }
}