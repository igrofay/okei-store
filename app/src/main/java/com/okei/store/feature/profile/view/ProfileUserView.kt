package com.okei.store.feature.profile.view

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.BackdropScaffold
import androidx.compose.material.BackdropValue
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.rememberBackdropScaffoldState
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.okei.store.R
import com.okei.store.feature.common.view.button.ToComeInVK
import com.okei.store.feature.profile.model.ProfileViewModel

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun ProfileUserView(
    viewModel: ProfileViewModel,
) {
    val profile by viewModel.profile
    BackdropScaffold(
        appBar = { },
        backLayerContent = {
            profile?.let {
                ProfileCardView(it)
            } ?: ToComeInVK()
        },
        frontLayerContent = {
            Column(
                modifier = Modifier
                    .fillMaxSize()
            ){
                Text(
                    text = stringResource(id = R.string.orders),
                    fontSize = 24.sp,
                    fontWeight = FontWeight.W600,
                    modifier = Modifier.padding(20.dp)
                )
                ListOrderView(viewModel.listOrder)
            }
        },
        peekHeight = 0.dp,
        frontLayerScrimColor = Color.Unspecified,
        backLayerBackgroundColor = MaterialTheme.colorScheme.primary.copy(0.6f),
        frontLayerBackgroundColor = MaterialTheme.colorScheme.background,
        modifier = Modifier
            .fillMaxSize(),
        scaffoldState = rememberBackdropScaffoldState(BackdropValue.Revealed),
    )

}