package com.okei.store.domain.model.error

sealed class OrderError : Error() {
    object RequiredProductDoesNotExistOrIsNotAvailableInThatQuantity : OrderError()
    object AnOrderWithThisIdExists : OrderError()
}