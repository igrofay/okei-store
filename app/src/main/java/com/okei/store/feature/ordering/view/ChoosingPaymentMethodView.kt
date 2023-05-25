package com.okei.store.feature.ordering.view

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Card
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.okei.store.R
import com.okei.store.domain.model.order.PaymentType


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ChoosingPaymentMethodView(
    paymentType: PaymentType,
    onPaymentTypeChange: (PaymentType) -> Unit,
) {
    var expanded by remember {
        mutableStateOf(false)
    }
    BoxWithConstraints {
        Card(
            onClick = {
                expanded = true
            }
        ){
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 12.dp),
                horizontalArrangement = Arrangement.spacedBy(10.dp),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Image(
                    painter = when (paymentType) {
                        PaymentType.Cash -> painterResource(R.drawable.image_cash)
                    },
                    contentDescription = null,
                    modifier = Modifier.size(28.dp)
                )
                Text(
                    text = stringResource(R.string.cash),
                    maxLines = 1,
                    fontSize = 18.sp,
                )
            }
        }
        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false },
            modifier = Modifier.width(this.maxWidth)
        ) {
            PaymentType.values().forEach { p1->
                DropdownMenuItem(
                    text = {
                        Text(
                            text = stringResource(R.string.cash),
                            maxLines = 1,
                            fontSize = 18.sp,
                            fontWeight = FontWeight.W400,
                        )
                    },
                    onClick = {
                        onPaymentTypeChange(p1)
                        expanded = false
                    },
                    modifier = Modifier.fillMaxWidth(),
                    leadingIcon = {
                        Image(
                            painter = when (paymentType) {
                                PaymentType.Cash -> painterResource(R.drawable.image_cash)
                            },
                            contentDescription = null,
                            modifier = Modifier.size(28.dp)
                        )
                    }
                )
            }
        }
    }
}