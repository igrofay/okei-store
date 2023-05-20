package com.okei.store.data.data_source.database

import android.content.SharedPreferences
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class TokenDatabase @Inject constructor(
    private val sharedPreferences: SharedPreferences,
) {
    fun getAccessToken() =
        sharedPreferences.getString("accessToken", null)

    fun setAccessToken(token: String?){
        sharedPreferences.edit()
            .putString("accessToken", token)
            .apply()
    }
}