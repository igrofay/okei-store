package com.okei.store.feature.cart.view

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.FilledIconButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.okei.store.R
import com.okei.store.domain.model.product.ProductModel

@Composable
fun ItemCartView(
    productModel: ProductModel,
    isAddedToCart: Boolean,
    quantityInCart: Int,
    minus: () -> Unit,
    plus: () -> Unit,
    onClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(144.dp)
            .clickable(onClick = onClick),
    ) {
        AsyncImage(
            model = productModel.mainImageUrl,
            contentDescription = null,
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .padding(end = 20.dp)
                .size(144.dp)
        )
        Column(
            modifier = Modifier
                .padding(vertical = 12.dp)
                .padding(end = 20.dp)
                .fillMaxHeight(),
            verticalArrangement = Arrangement.SpaceBetween,
        ) {
            Text(
                text = productModel.name,
                maxLines = 3,
                fontSize = 16.sp,
                fontWeight = FontWeight.Medium,
                color = MaterialTheme.colorScheme.onSurface,
                modifier = Modifier.fillMaxWidth(),
            )
            Row {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    FilledIconButton(
                        onClick = minus,
                        modifier = Modifier.size(28.dp),
                        shape = MaterialTheme.shapes.small,
                        enabled = 0 < quantityInCart,
                    ) {
                        Icon(
                            painter = painterResource(id = R.drawable.ic_remove),
                            contentDescription = null
                        )
                    }
                    Text(
                        text = quantityInCart.toString(),
                        fontSize = 16.sp,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
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
                }
                Spacer(modifier = Modifier.weight(1f))
                Text(
                    text = "${productModel.price} ₽",
                    fontSize = 16.sp,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    fontWeight = FontWeight.Medium,
                )
            }
        }
    }
}