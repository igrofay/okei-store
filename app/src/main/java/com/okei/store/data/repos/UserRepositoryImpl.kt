package com.okei.store.data.repos

import com.okei.store.data.data_source.api.vk.VKApi
import com.okei.store.data.data_source.database.UserDatabase
import com.okei.store.domain.model.user.UserModel
import com.okei.store.domain.repos.UserRepository
import com.vk.api.sdk.exceptions.VKApiExecutionException
import kotlinx.coroutines.delay
import javax.inject.Inject

class UserRepositoryImpl @Inject  constructor(
    private val vkApi: VKApi,
    private val userDatabase: UserDatabase
): UserRepository {
    override suspend fun getUser(): UserModel? {
        return try {
            vkApi.getProfile().fromVKUserToUserModel()
        }catch (e: VKApiExecutionException){ null }
    }

    override fun isUserAuthorized() =
        userDatabase.getIsAuthorized()

    override fun setUserIsAuth(isAuth: Boolean) {
        userDatabase.setIsAuthorized(isAuth)
    }
}