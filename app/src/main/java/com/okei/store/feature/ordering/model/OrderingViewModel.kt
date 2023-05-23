package com.okei.store.feature.ordering.model

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.okei.store.R
import com.okei.store.domain.repos.UserRepository
import com.okei.store.feature.cart.model.ProfileSideEffect
import com.okei.store.feature.common.model.UserStateNotification
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class OrderingViewModel @Inject constructor(
    private val userRepository: UserRepository,
): ViewModel(), UserStateNotification.UserStateListener {
    private val _state = mutableStateOf(OrderingState.Load)
    val state: State<OrderingState> = _state
    private val _sideEffect = Channel<OrderingSideEffect>()
    val sideEffect : Flow<OrderingSideEffect>
        get() = _sideEffect.receiveAsFlow()
    override fun onStateChange(state: UserStateNotification.State) {
        when (state) {
            UserStateNotification.State.Authorized -> loadOrder()
            UserStateNotification.State.NoLongerAuthorized -> {
                _state.value = OrderingState.NeedToSignIn
            }
            UserStateNotification.State.ErrorHasOccurred -> viewModelScope.launch {
                _sideEffect.send(OrderingSideEffect.Message(R.string.error_has_occurred))
            }
        }
    }
    private fun loadOrder(){
        if (userRepository.isUserAuthorized()){

        }else{
            _state.value = OrderingState.NeedToSignIn
        }
    }
}