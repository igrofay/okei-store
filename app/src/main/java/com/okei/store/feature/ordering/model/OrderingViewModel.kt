package com.okei.store.feature.ordering.model

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.okei.store.R
import com.okei.store.domain.model.error.AppError
import com.okei.store.domain.model.error.OrderError
import com.okei.store.domain.model.order.PaymentType
import com.okei.store.domain.repos.UserRepository
import com.okei.store.domain.use_case.order.CreateOrderUseCase
import com.okei.store.feature.app.model.CheckingPermissions
import com.okei.store.feature.common.model.CreateOrderNotification
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
    private val createOrderUseCase: CreateOrderUseCase,
    private val createOrderNotification: CreateOrderNotification,
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
    private val _isCreating = mutableStateOf(false)
    val isCreating : State<Boolean> = _isCreating
    init {
        userStateNotification.add(id, this)
        subscribeToDistance()
        initState()
    }
    override fun onStateChange(state: UserStateNotification.State) {
        when (state) {
            UserStateNotification.State.Authorized -> initState()
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
    private fun initState(){
        if (userRepository.isUserAuthorized()){
            _state.value = OrderingState.OrderingData
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
    fun createOrder() {
        _isCreating.value = true
        viewModelScope.launch {
            createOrderUseCase.execute(_paymentType.value)
                .onSuccess {
                    createOrderNotification.notifyAboutCreatedProduct()
                    _sideEffect.send(OrderingSideEffect.CreatedOrder)
                }.onFailure {
                    when(it){
                        OrderError.AnOrderWithThisIdExists-> createOrder()
                        OrderError.RequiredProductDoesNotExistOrIsNotAvailableInThatQuantity ->{
                            viewModelScope.launch {
                                _sideEffect.send(
                                    OrderingSideEffect.Message(R.string.there_are_not_so_many_products_in_stock)
                                )
                            }
                            _isCreating.value = false
                        }
                        AppError.NoNetworkAccess -> {
                            _sideEffect.send(
                                OrderingSideEffect.Message(R.string.lack_of_access_to_internet)
                            )
                            _isCreating.value = false
                        }
                        else-> _isCreating.value = false
                    }
                }
        }
    }
}