package com.okei.store.feature.shop.view

import androidx.compose.animation.core.CubicBezierEasing
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Icon
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.okei.store.domain.model.product.ProductModel
import com.okei.store.feature.shop.model.ShopViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchProductView(
    viewModel: ShopViewModel,
) {
    val isSearching by viewModel.isSearching
    val query by viewModel.query
    val animatePadding by animateDpAsState(
        targetValue = if (isSearching) 0.dp else 20.dp,
        animationSpec = tween(
            durationMillis = 350,
            easing = CubicBezierEasing(0.05f, 0.7f, 0.1f, 1.0f),
        )
    )
    SearchBar(
        query = query,
        onQueryChange = viewModel::inputQuery,
        onSearch = viewModel::search,
        active = isSearching,
        onActiveChange = {
            when (it) {
                true -> viewModel.openSearch()
                false -> viewModel.cancelSearch()
            }
        },
        trailingIcon = {
            if (isSearching && query.isNotEmpty())
                IconButton(onClick = { viewModel.inputQuery("") }) {
                    Icon(
                        imageVector = Icons.Default.Close,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
        },
        modifier = Modifier.padding(horizontal = animatePadding)
    ) {
        LazyColumn(
            contentPadding = PaddingValues(
                horizontal = 0.dp
            )
        ) {
            items(viewModel.foundProducts) {
                ItemFoundProduct(productModel = it) {
                    viewModel.showProductModel(it)
                }
                Divider()
            }
        }
    }
}

@Composable
internal fun ItemFoundProduct(
    productModel: ProductModel,
    onClick: () -> Unit,
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
            .padding(vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        AsyncImage(
            model = productModel.mainImageUrl,
            contentDescription = null,
            modifier = Modifier
                .padding(horizontal = 16.dp)
                .size(56.dp),
            contentScale = ContentScale.Crop
        )
        Text(
            text = productModel.name,
            fontSize = 16.sp,
            fontWeight = FontWeight.Medium,
            modifier = Modifier.weight(1f),
            maxLines = 3,
            overflow = TextOverflow.Ellipsis,
            color = MaterialTheme.colorScheme.onSurface
        )
        Text(
            text = "${productModel.price} ₽",
            fontSize = 14.sp,
            modifier = Modifier.padding(
                start = 16.dp,
                end = 24.dp
            ),
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
    }
}