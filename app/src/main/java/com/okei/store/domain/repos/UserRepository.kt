package com.okei.store.domain.repos

import com.okei.store.domain.model.user.UserModel

interface UserRepository {
    suspend fun getUser() : UserModel
    fun isUserAuthorized() : Boolean
    fun setUserIsAuth(isAuth: Boolean)
}