package com.okei.store.feature.nav.model

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.SheetState
import androidx.compose.material3.SheetValue
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf

@OptIn(ExperimentalMaterial3Api::class)
class BottomSheetManager(
    private val sheetState: SheetState,
) {
    private val _content = mutableStateOf<@Composable ()-> Unit>({})
    val content : State<@Composable ()-> Unit> = _content

    suspend fun show(content: @Composable () -> Unit){
        _content.value = content
        sheetState.expand()
    }

    @Composable
    fun collectAsState(onStateChange: (SheetValue)-> Unit) = LaunchedEffect(sheetState.currentValue){
        onStateChange(sheetState.currentValue)
    }
    fun onClose(){
         _content.value = {}
    }
    suspend fun close(){
        sheetState.hide()
        onClose()
    }
}