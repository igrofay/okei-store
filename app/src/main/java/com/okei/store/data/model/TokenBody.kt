package com.okei.store.data.model

import com.okei.store.domain.model.auth.TokenModel
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class TokenBody(
    @SerialName("token")
    override val accessToken: String
) : TokenModel