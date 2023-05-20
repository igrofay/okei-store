package com.okei.store.feature.app.view

import android.app.Application
import androidx.activity.viewModels
import com.okei.store.domain.repos.UserRepository
import com.okei.store.domain.use_case.auth.ExitUseCase
import com.okei.store.feature.common.model.UserStateNotification
import com.vk.api.sdk.VK
import com.vk.api.sdk.VKTokenExpiredHandler
import dagger.hilt.android.HiltAndroidApp
import javax.inject.Inject


@HiltAndroidApp
class App : Application(){
    @Inject
    lateinit var userRepository: UserRepository
    @Inject
    lateinit var userStateNotification: UserStateNotification
    @Inject
    lateinit var exitUseCase: ExitUseCase
    override fun onCreate() {
        super.onCreate()
        VK.addTokenExpiredHandler(tokenTracker)
    }
    private val tokenTracker = object: VKTokenExpiredHandler {
        override fun onTokenExpired() {
            exitUseCase.execute()
            userStateNotification
                .changeState(UserStateNotification.State.NoLongerAuthorized)
        }
    }
}