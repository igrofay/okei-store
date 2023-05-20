package com.okei.store.domain.use_case.auth

import com.okei.store.domain.repos.AppRepository
import com.okei.store.domain.repos.UserRepository
import com.vk.api.sdk.VK
import com.vk.api.sdk.VKKeyValueStorage
import com.vk.api.sdk.VKPreferencesKeyValueStorage
import com.vk.api.sdk.auth.VKAccessToken
import javax.inject.Inject

class ExitUseCase @Inject constructor(
    private val appRepository: AppRepository,
    private val userRepository: UserRepository,
){
    fun execute() = runCatching {
        appRepository.clear()
        userRepository.setUserIsAuth(false)
    }
}