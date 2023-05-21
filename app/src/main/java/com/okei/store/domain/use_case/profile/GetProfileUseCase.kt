package com.okei.store.domain.use_case.profile

import com.okei.store.domain.repos.UserRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class GetProfileUseCase @Inject constructor(
    private val userRepository: UserRepository,
) {
    suspend fun execute() = runCatching {
        withContext(Dispatchers.Default){ userRepository.getUser() }
    }
}