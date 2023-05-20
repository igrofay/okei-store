package com.okei.store.data.model

import kotlinx.serialization.Serializable

@Serializable
data class AuthBody(
    val userId: String,
    val deviceId: String
)
