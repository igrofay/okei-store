package com.okei.store.feature.app.model

import androidx.lifecycle.ViewModel
import com.okei.store.domain.repos.UserRepository
import com.okei.store.feature.common.model.UserStateNotification
import com.vk.api.sdk.auth.VKAuthenticationResult
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject


@HiltViewModel
class OkeiStoreVM @Inject constructor(
    private val userStateNotification : UserStateNotification,
    private val userRepository: UserRepository,
) : ViewModel(){


    fun vkAuthenticationResult(result: VKAuthenticationResult){
        when (result) {
            is VKAuthenticationResult.Success -> {
                userRepository.setUserIsAuth(true)
                userStateNotification.changeState(UserStateNotification.State.Authorized)
            }
            is VKAuthenticationResult.Failed -> {}
        }
    }
}