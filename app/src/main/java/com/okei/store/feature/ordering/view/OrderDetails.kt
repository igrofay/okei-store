package com.okei.store.feature.ordering.view

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Button
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
                .padding(bottom = 18.dp)
                .fillMaxWidth()
                .padding(horizontal = 20.dp)
        )
        LazyColumn(
            contentPadding = PaddingValues(
                horizontal = 20.dp
            ),
            modifier = Modifier.fillMaxWidth().weight(1f),
            verticalArrangement = Arrangement.spacedBy(24.dp)
        ){
            item {
                Column(
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    Text(
                        text = stringResource(R.string.here_you_can_pick_up_your_order),
                        fontSize = 18.sp,
                        fontWeight = FontWeight.W700,
                        modifier = Modifier.fillMaxWidth(0.8f),
                        lineHeight = 18.sp,
                    )
                    OkeiMapView(viewModel.distance.value)
                    Text(
                        text = stringResource(R.string.order_can_be_picked_up),
                        fontSize = 12.sp,
                        fontWeight = FontWeight.W400,
                        lineHeight = 16.sp,
                    )
                }
            }
            item {
                Column(
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    Text(
                        text = stringResource(R.string.select_payment_method),
                        fontSize = 18.sp,
                        fontWeight = FontWeight.W700,
                        modifier = Modifier.fillMaxWidth(0.8f),
                        lineHeight = 18.sp,
                    )
                    ChoosingPaymentMethodView(
                        paymentType = viewModel.paymentType.value,
                        onPaymentTypeChange = { paymentType ->

                        }
                    )
                }
            }
        }
        Button(
            onClick = {

            },
            modifier = Modifier
                .padding(horizontal = 20.dp, vertical = 24.dp)
                .fillMaxWidth(),
        ) {
            Text(
                text = stringResource(id = R.string.place_an_order),
                fontSize = 16.sp
            )
        }
    }
}