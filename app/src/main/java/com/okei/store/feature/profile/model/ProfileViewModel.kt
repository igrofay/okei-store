package com.okei.store.feature.profile.model

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.okei.store.R
import com.okei.store.domain.model.user.UserModel
import com.okei.store.domain.repos.UserRepository
import com.okei.store.feature.cart.model.ProfileSideEffect
import com.okei.store.feature.common.model.UserStateNotification
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.consumeAsFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val userRepository: UserRepository,
    private val userStateNotification: UserStateNotification,
) : ViewModel() , UserStateNotification.UserStateListener{
    private val _sideEffect = Channel<ProfileSideEffect>()
    val sideEffect: Flow<ProfileSideEffect>
        get() = _sideEffect.receiveAsFlow()
    private val _profile = mutableStateOf<UserModel?>(null)
    val profile: State<UserModel?> = _profile

    private val _state = mutableStateOf(UserState.Loading)
    val state: State<UserState> = _state

    init {
        userStateNotification.add(id, this)
        loadUser()
    }

    private fun loadUser(){
        if (userRepository.isUserAuthorized()){
            viewModelScope.launch {
                val userModel = withContext(Dispatchers.Default){ userRepository.getUser() }
                if (userModel != null){
                    withContext(Dispatchers.Main){
                        _profile.value = userModel
                        _state.value = UserState.UserData
                    }
                }else{
                    userRepository.setUserIsAuth(false)
                    _sideEffect.send(ProfileSideEffect.Message(R.string.error_has_occurred))
                }
            }
        }else{
            _state.value = UserState.UserData
        }
    }
    companion object{
        private const val id = "ProfileViewModel"
    }

    override fun onStateChange(state: UserStateNotification.State) {
        when(state){
            UserStateNotification.State.Authorized -> loadUser()
            UserStateNotification.State.NoLongerAuthorized -> clear()
        }
    }

    private fun clear(){
        _profile.value = null
    }

    override fun onCleared() {
        super.onCleared()
        userStateNotification.remove(id)
    }
}