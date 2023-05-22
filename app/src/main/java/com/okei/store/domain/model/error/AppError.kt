package com.okei.store.domain.model.error

sealed class AppError : Error(){
    object NoNetworkAccess : AppError()
}