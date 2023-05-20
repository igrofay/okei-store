package com.okei.store.domain.repos

import com.okei.store.domain.model.auth.TokenModel

interface AuthRepository {
    suspend fun authUser(idUser: Long) : TokenModel
}