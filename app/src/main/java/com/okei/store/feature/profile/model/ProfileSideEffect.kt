package com.okei.store.feature.profile.model

import androidx.annotation.StringRes

sealed class ProfileSideEffect{
    class Message(@StringRes val stringRes: Int) : ProfileSideEffect()
}
