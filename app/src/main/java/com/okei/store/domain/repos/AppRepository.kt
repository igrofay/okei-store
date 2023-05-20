package com.okei.store.domain.repos

interface AppRepository {
    fun setAccessToken(token: String)
    fun getAccessToken() : String?
    fun clear()
}