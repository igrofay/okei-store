package com.okei.store.feature.profile.view

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.Divider
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.okei.store.R
import com.okei.store.domain.model.order.OrderModel

@Composable
fun ListOrderView(
    listOrder: List<OrderModel>,
) {
    LazyVerticalGrid(
        columns = GridCells.Adaptive(300.dp),
        modifier = Modifier
            .fillMaxSize(),
        contentPadding = PaddingValues(
            horizontal = 20.dp,
            vertical = 12.dp,
        ),
        verticalArrangement = Arrangement
            .spacedBy(16.dp)
    ) {
        items(listOrder) {
            CardOrder(order = it)
        }
    }
}

@Composable
private fun CardOrder(
    order: OrderModel,
) {
    ElevatedCard {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(
                    vertical = 12.dp,
                ),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 14.dp),
                verticalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                Text(
                    text = stringResource(R.string.order_for_amount) + " ${order.sum} ₽",
                    fontSize = 22.sp,
                    fontWeight = FontWeight.W700,
                    lineHeight = 20.sp
                )
                Text(
                    text = order.date,
                    fontSize = 14.sp,
                )
                Text(
                    text = stringResource(R.string.order_code) + ": ${order.code}",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.W600
                )
            }
            Divider()
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 14.dp),
                verticalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                for (item in order.product){
                    Row {
                        Text(
                            text = item.key,
                            fontSize = 12.sp,
                            fontWeight = FontWeight.W300,
                            modifier = Modifier.weight(1f)
                        )
                        Text(
                            text = item.value,
                            fontSize = 12.sp,
                            fontWeight = FontWeight.W300,
                        )
                    }
                }
            }
        }
    }
}