package com.okei.store.feature.app.model

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.okei.store.domain.use_case.auth.AuthUseCase
import com.okei.store.feature.common.model.UserStateNotification
import com.vk.api.sdk.auth.VKAuthenticationResult
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class OkeiStoreVM @Inject constructor(
    private val userStateNotification: UserStateNotification,
    private val authUseCase: AuthUseCase,
) : ViewModel() {


    fun vkAuthenticationResult(result: VKAuthenticationResult) {
        when (result) {
            is VKAuthenticationResult.Success -> {
                Log.e("OkeiStoreVM", "1")
                viewModelScope.launch {
                    authUseCase.execute(result.token.userId.value)
                        .onSuccess {
                            Log.e("OkeiStoreVM", "2-s")
                            userStateNotification.changeState(UserStateNotification.State.Authorized)
                        }
                        .onFailure {
                            Log.e("OkeiStoreVM", "2-e")
                            Log.e("OkeiStoreVM", it.message.toString())
                            userStateNotification.changeState(UserStateNotification.State.ErrorHasOccurred)
                        }
                }
            }

            is VKAuthenticationResult.Failed -> {
                userStateNotification.changeState(UserStateNotification.State.ErrorHasOccurred)
            }
        }
    }
}