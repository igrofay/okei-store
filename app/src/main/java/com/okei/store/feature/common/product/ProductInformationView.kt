package com.okei.store.feature.common.product

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.PageSize
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.okei.store.domain.model.product.ProductModel
import kotlin.math.absoluteValue
import com.okei.store.R


@OptIn(ExperimentalFoundationApi::class)
@Composable
fun ProductInformationView(
    productModel: ProductModel,
    isAddedToCart: Boolean,
    quantityInCart: Int,
    minus: () -> Unit,
    plus: () -> Unit,
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .verticalScroll(rememberScrollState()),
        verticalArrangement = Arrangement.spacedBy(20.dp)
    ) {
        Text(
            text = productModel.name,
            color = MaterialTheme.colorScheme.onSurface,
            fontSize = 20.sp,
            fontWeight = FontWeight.W600,
            modifier = Modifier
                .fillMaxWidth(0.775f)
                .padding(horizontal = 20.dp)
        )
        val pagerState = rememberPagerState(
            initialPage = 0,
            initialPageOffsetFraction = 0f
        ){
            productModel.listImageUrl.size
        }
        BoxWithConstraints(
            modifier = Modifier.fillMaxWidth()
        ) {
            val width = 230.dp
            val height = 250.dp
            HorizontalPager(
                state = pagerState,
                pageSize = PageSize.Fixed(width),
                pageSpacing = 24.dp,
                contentPadding = PaddingValues(
                    horizontal = maxWidth / 2 - width / 2
                )
            ) { page ->
                Card(
                    Modifier
                        .width(width)
                        .height(height)
                        .graphicsLayer {
                            val pageOffset = (
                                    (pagerState.currentPage - page) + pagerState
                                        .currentPageOffsetFraction
                                    ).absoluteValue

                            // We animate the alpha, between 50% and 100%
                            alpha = lerp(
                                start = 0.5f,
                                stop = 1f,
                                fraction = 1f - pageOffset.coerceIn(0f, 1f)
                            )
                        }
                ) {
                    AsyncImage(
                        model = productModel.listImageUrl[page],
                        contentDescription = null,
                        modifier = Modifier
                            .width(width)
                            .height(height),
                        contentScale = ContentScale.Crop
                    )
                }
            }
        }
        Text(
            text = productModel.description,
            modifier = Modifier
                .padding(horizontal = 20.dp),
            fontSize = 14.sp,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
        if (isAddedToCart){
            Row(
                modifier = Modifier
                    .padding(horizontal = 20.dp)
                    .padding(bottom = 22.dp)
                    .fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceAround,
                verticalAlignment = Alignment.CenterVertically
            ){
                FilledIconButton(
                    onClick = minus,
                    modifier = Modifier.size(40.dp),
                    shape = MaterialTheme.shapes.small,
                    enabled =  0 < quantityInCart ,
                ) {
                    Icon(painter = painterResource(id = R.drawable.ic_remove), contentDescription = null)
                }

                Text(
                    text = quantityInCart.toString(),
                    fontSize = 20.sp,
                    color = MaterialTheme.colorScheme.onSurface,
                    fontWeight = FontWeight.Medium,
                )
                FilledIconButton(
                    onClick = plus,
                    modifier = Modifier.size(40.dp),
                    shape = MaterialTheme.shapes.small,
                    enabled = productModel.quantityInStock > quantityInCart
                ) {
                    Icon(imageVector = Icons.Default.Add, contentDescription = null)
                }
            }
        }else{
            Button(
                onClick = plus,
                modifier = Modifier
                    .padding(horizontal = 20.dp)
                    .padding(bottom = 22.dp)
                    .fillMaxWidth()
            ) {
                Text(
                    text = "${stringResource(id = R.string.add_for)} ${productModel.price} ₽",
                    fontSize = 16.sp
                )
            }
        }

    }
}

private fun lerp(start: Float, stop: Float, fraction: Float): Float {
    return (1 - fraction) * start + fraction * stop
}