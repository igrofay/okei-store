package com.okei.store.data.repos

import com.okei.store.data.data_source.database.TokenDatabase
import com.okei.store.domain.repos.AppRepository
import javax.inject.Inject

class AppRepositoryImpl @Inject constructor(
    private val tokenDatabase: TokenDatabase
) : AppRepository{
    override fun setAccessToken(token: String) {
        tokenDatabase.setAccessToken(token)
    }

    override fun getAccessToken(): String? {
        return tokenDatabase.getAccessToken()
    }

    override fun clear() {
        tokenDatabase.setAccessToken(null)
    }
}