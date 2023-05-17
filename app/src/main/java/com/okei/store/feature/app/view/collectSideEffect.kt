package com.okei.store.feature.app.view

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.platform.LocalLifecycleOwner
import kotlinx.coroutines.flow.Flow

@Composable
fun <T> Flow<T>.collectSideEffect(onSideEffectChange: suspend (T)->Unit){
    val lifecycle = LocalLifecycleOwner.current
    val sideEffect by collectAsState(null)
    LaunchedEffect(lifecycle.lifecycle, sideEffect){
        sideEffect?.let {
            onSideEffectChange(it)
        }
    }
}