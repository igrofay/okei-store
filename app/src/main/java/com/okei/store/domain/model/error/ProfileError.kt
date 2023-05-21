package com.okei.store.domain.model.error

sealed class ProfileError : Error() {
    object JsonProcessingError : ProfileError()
    object VKApiError: ProfileError()
    object NoNetworkAccess : ProfileError()
}