package com.okei.store.feature.ordering.model

import androidx.annotation.StringRes

sealed class OrderingSideEffect{
    class Message(@StringRes val stringRes: Int) : OrderingSideEffect()
    object RequestAccessToLocation : OrderingSideEffect()

    object CreatedOrder : OrderingSideEffect()
}
