package com.okei.store.feature.profile.model

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.okei.store.R
import com.okei.store.domain.model.error.AppError
import com.okei.store.domain.model.error.ProfileError
import com.okei.store.domain.model.order.OrderModel
import com.okei.store.domain.model.user.UserModel
import com.okei.store.domain.repos.UserRepository
import com.okei.store.domain.use_case.auth.ExitUseCase
import com.okei.store.domain.use_case.order.GetOrdersUseCase
import com.okei.store.domain.use_case.profile.GetProfileUseCase
import com.okei.store.feature.common.model.UserStateNotification
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val userRepository: UserRepository,
    private val userStateNotification: UserStateNotification,
    private val exitUseCase: ExitUseCase,
    private val getProfileUseCase: GetProfileUseCase,
    private val getOrdersUseCase: GetOrdersUseCase,
) : ViewModel(), UserStateNotification.UserStateListener {
    private val _sideEffect = Channel<ProfileSideEffect>()
    val sideEffect: Flow<ProfileSideEffect>
        get() = _sideEffect.receiveAsFlow()
    private val _profile = mutableStateOf<UserModel?>(null)
    val profile: State<UserModel?> = _profile

    private val _state = mutableStateOf(UserState.Loading)
    val state: State<UserState> = _state

    private val _listOrder = mutableStateListOf<OrderModel>()
    val listOrder: List<OrderModel> = _listOrder

    init {
        userStateNotification.add(id, this)
    }

    fun refresh() = loadUserData()

    private fun loadUserData() {
        if (userRepository.isUserAuthorized()) {
            viewModelScope.launch {
                getProfileUseCase.execute()
                    .onSuccess { userModel ->
                        _profile.value = userModel
                        _state.value = UserState.UserData
                    }.onFailure {
                        when (it) {
                            ProfileError.VKApiError -> {
                                exitUseCase.execute()
                                _sideEffect.send(ProfileSideEffect.Message(R.string.error_has_occurred))
                            }
                            ProfileError.JsonProcessingError -> {
                                _sideEffect.send(ProfileSideEffect.Message(R.string.error_has_occurred))
                            }
                            AppError.NoNetworkAccess->{
                                _sideEffect.send(ProfileSideEffect.Message(R.string.lack_of_access_to_internet))
                            }
                        }
                    }
            }
            viewModelScope.launch {
                getOrdersUseCase.execute()
                    .onSuccess {
                        _listOrder.clear()
                        _listOrder.addAll(it)
                    }.onFailure {
                        when(it){
                            AppError.NoNetworkAccess->{
                                _sideEffect.send(ProfileSideEffect.Message(R.string.lack_of_access_to_internet))
                            }
                        }
                    }
            }
        } else {
            _state.value = UserState.UserData
        }
    }

    companion object {
        private const val id = "ProfileViewModel"
    }

    override fun onStateChange(state: UserStateNotification.State) {
        when (state) {
            UserStateNotification.State.Authorized -> loadUserData()
            UserStateNotification.State.NoLongerAuthorized -> clear()
            UserStateNotification.State.ErrorHasOccurred -> viewModelScope.launch {
                _sideEffect.send(ProfileSideEffect.Message(R.string.error_has_occurred))
            }
        }
    }

    private fun clear() {
        _profile.value = null
    }

    override fun onCleared() {
        super.onCleared()
        _sideEffect.close()
        userStateNotification.remove(id)
    }
}