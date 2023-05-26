package com.okei.store.domain.use_case.auth

import com.okei.store.domain.repos.AppRepository
import com.okei.store.domain.repos.AuthRepository
import com.okei.store.domain.repos.UserRepository
import javax.inject.Inject

class AuthUseCase @Inject constructor(
    private val appRepository: AppRepository,
    private val authRepository: AuthRepository,
    private val userRepository: UserRepository,
) {
    suspend fun execute(idUser: Long) = runCatching {
        val model = authRepository.authUser(idUser)
        appRepository.setAccessToken(model.accessToken)
        userRepository.setUserIsAuth(true)
    }
}