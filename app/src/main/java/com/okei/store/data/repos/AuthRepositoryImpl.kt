package com.okei.store.data.repos


import android.annotation.SuppressLint
import android.content.Context
import android.provider.Settings.Secure
import com.okei.store.data.data_source.api.server.AuthApi
import com.okei.store.data.model.AuthBody
import com.okei.store.data.utils.ToSHA
import com.okei.store.domain.model.auth.TokenModel
import com.okei.store.domain.repos.AuthRepository
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject


class AuthRepositoryImpl @Inject constructor(
    private val authApi: AuthApi,
    @ApplicationContext
    private val context: Context,
) : AuthRepository {
    @SuppressLint("HardwareIds")
    override suspend fun authUser(idUser: Long): TokenModel {
        val androidId = Secure.getString(
            context.contentResolver,
            Secure.ANDROID_ID
        )
        return authApi.sign(
            AuthBody(
                userId = ToSHA.toSHA256(idUser),
                deviceId = ToSHA.toSHA256(androidId)
            )
        )
    }
}