package com.okei.store.data.data_source.database

import android.content.SharedPreferences
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class UserDatabase @Inject constructor(
    private val sharedPreferences: SharedPreferences,
){
    fun getIsAuthorized () =
        sharedPreferences.getBoolean("isAuthorized", false)

    fun setIsAuthorized(isAuthorized: Boolean){
        sharedPreferences.edit()
            .putBoolean("isAuthorized", isAuthorized)
            .apply()
    }
}