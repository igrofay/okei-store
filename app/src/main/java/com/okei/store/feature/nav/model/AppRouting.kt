package com.okei.store.feature.nav.model

sealed class AppRouting(val route: String) {
    object MainContent : AppRouting("main_content")
    object AccountLogin : AppRouting("account_login")
    object Ordering : AppRouting("ordering")
}