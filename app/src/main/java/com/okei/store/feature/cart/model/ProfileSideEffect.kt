package com.okei.store.feature.cart.model

import androidx.annotation.StringRes

sealed class ProfileSideEffect{
    class Message(@StringRes val stringRes: Int) : ProfileSideEffect()
}
