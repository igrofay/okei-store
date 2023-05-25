package com.okei.store.feature.ordering.view

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.okei.store.R
import com.okei.store.feature.ordering.model.OrderingViewModel

@Composable
fun OrderDetails(
    viewModel: OrderingViewModel,
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(top = 32.dp)
    ){
        Text(
            text = stringResource(id = R.string.ordering),
            fontSize = 24.sp,
            fontWeight = FontWeight.W700,
            modifier = Modifier
                .padding(bottom = 12.dp)
                .fillMaxWidth()
                .padding(horizontal = 20.dp)
        )
        LazyColumn(
            contentPadding = PaddingValues(
                horizontal = 20.dp
            ),
            modifier = Modifier.fillMaxSize()
        ){
            item {
                OkeiMapView(viewModel.distance.value)
            }
        }
    }
}