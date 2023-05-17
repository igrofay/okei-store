package com.okei.store.feature.profile.view

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.rememberScaffoldState
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.ViewModel
import com.okei.store.feature.app.view.collectSideEffect
import com.okei.store.feature.cart.model.ProfileSideEffect
import com.okei.store.feature.profile.model.ProfileViewModel
import com.okei.store.feature.profile.model.UserState

@Composable
fun ProfileScreen(
    viewModel: ProfileViewModel = hiltViewModel(),
) {
    val res = LocalContext.current.resources
    val snackbarHostState = remember { SnackbarHostState() }
    val profileState by viewModel.state
    viewModel.sideEffect.collectSideEffect{ sideEffect->
        when(sideEffect){
            is ProfileSideEffect.Message -> {
                snackbarHostState.showSnackbar(
                    res.getString(sideEffect.stringRes)
                )
            }
        }
    }
    Scaffold(
        snackbarHost ={
            SnackbarHost(snackbarHostState)
        }
    ) {
        Box(modifier = Modifier
            .padding(it)
            .fillMaxSize()
        ){
            when(profileState){
                UserState.Loading -> Box(
                    modifier = Modifier
                        .fillMaxSize(),
                    contentAlignment = Alignment.Center
                ){
                    CircularProgressIndicator()
                }
                UserState.UserData -> ProfileUserView(
                    viewModel,
                )
            }
        }

    }
}