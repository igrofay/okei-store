package com.okei.store.feature.ordering.model

import android.Manifest
import android.content.pm.PackageManager
import android.util.Log
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.core.app.ActivityCompat
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.okei.store.R
import com.okei.store.domain.model.order.PaymentType
import com.okei.store.domain.repos.UserRepository
import com.okei.store.feature.app.model.CheckingPermissions
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
    private val userStateNotification: UserStateNotification,
    private val locationClient: LocationClient,
    private val checkingPermissions: CheckingPermissions,
): ViewModel(), UserStateNotification.UserStateListener {
    private val _state = mutableStateOf(OrderingState.OrderingData)
    val state: State<OrderingState> = _state
    private val _sideEffect = Channel<OrderingSideEffect>()
    private val _distance = mutableStateOf<Double?>(null)
    val distance : State<Double?> = _distance
    val sideEffect : Flow<OrderingSideEffect>
        get() = _sideEffect.receiveAsFlow()
    private val _paymentType = mutableStateOf(PaymentType.Cash)
    val paymentType : State<PaymentType> = _paymentType

    init {
        userStateNotification.add(id, this)
        subscribeToDistance()
    }
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
    companion object{
        private const val id = "OrderingViewModel"
    }
    private fun loadOrder(){
        if (userRepository.isUserAuthorized()){

        }else{
            _state.value = OrderingState.NeedToSignIn
        }
    }
    fun subscribeToDistance(){
        if (
            !checkingPermissions.checkLocationAccessPermission()
        ) {
            locationClient.subscribeToDistanceChange {
                _distance.value = it
            }
        }else{
            viewModelScope.launch {
                _sideEffect.send(OrderingSideEffect.RequestAccessToLocation)
            }
        }

    }

    override fun onCleared() {
        super.onCleared()
        userStateNotification.remove(id)
    }
}