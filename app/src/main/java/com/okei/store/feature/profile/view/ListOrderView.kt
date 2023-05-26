package com.okei.store.feature.profile.view

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.okei.store.domain.model.order.OrderModel

@Composable
fun ListOrderView(
    listOrder: List<OrderModel>,
) {
    LazyColumn(
        modifier = Modifier
            .fillMaxSize(),
        contentPadding = PaddingValues(
            horizontal = 20.dp,
            vertical = 12.dp,
        )
    ){
        items(listOrder){

        }
    }
}

@Composable
private fun CardOrder(
    order: OrderModel,
) {

}