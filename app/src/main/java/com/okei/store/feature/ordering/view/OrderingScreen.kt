package com.okei.store.feature.ordering.view

import android.Manifest
import android.annotation.SuppressLint
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.hilt.navigation.compose.hiltViewModel
import com.okei.store.feature.app.view.collectSideEffect
import com.okei.store.feature.ordering.model.OrderingSideEffect
import com.okei.store.feature.ordering.model.OrderingState
import com.okei.store.feature.ordering.model.OrderingViewModel

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun OrderingScreen(
    viewModel: OrderingViewModel = hiltViewModel(),
) {
    val requestPermission = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission(),
        onResult = {
            if (it){
                viewModel.subscribeToDistance()
            }
        }
    )
    val state by viewModel.state
    val snackbarHostState = remember { SnackbarHostState() }
    val res = LocalContext.current.resources
    viewModel.sideEffect.collectSideEffect{ sideEffect->
        when(sideEffect){
            is OrderingSideEffect.Message -> snackbarHostState.showSnackbar(
                res.getString(sideEffect.stringRes)
            )
            OrderingSideEffect.RequestAccessToLocation -> {
                requestPermission.launch(Manifest.permission.ACCESS_FINE_LOCATION)
                requestPermission.launch(Manifest.permission.ACCESS_COARSE_LOCATION)
            }
        }
    }
    Scaffold(
        snackbarHost ={
            SnackbarHost(snackbarHostState)
        }
    ) {
        when(state){
            OrderingState.Load -> Box(
                modifier = Modifier
                    .fillMaxSize(),
                contentAlignment = Alignment.Center
            ){
                CircularProgressIndicator()
            }
            OrderingState.NeedToSignIn -> LoginAccount()
            OrderingState.OrderingData -> OrderDetails(viewModel)
        }
    }
}