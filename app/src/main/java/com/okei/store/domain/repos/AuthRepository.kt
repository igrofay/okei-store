package com.okei.store.domain.repos

interface AuthRepository {
    fun authUser(idUser: Int) : String
}