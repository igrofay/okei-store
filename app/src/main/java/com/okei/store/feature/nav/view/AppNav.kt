package com.okei.store.feature.nav.view

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.okei.store.feature.nav.model.AppRouting
import com.okei.store.feature.ordering.view.OrderingScreen


@Composable
fun AppNav() {
    val appController = rememberNavController()
    NavHost(
        navController = appController,
        startDestination = AppRouting.MainContent.route
    ){
        composable(AppRouting.MainContent.route){
            MainContentNav(
                createOrder = {
                    appController.navigate(AppRouting.Ordering.route)
                }
            )
        }
        composable(AppRouting.AccountLogin.route){}
        composable(AppRouting.Ordering.route){
            OrderingScreen(
                created = {
                    appController.popBackStack()
                }
            )
        }
    }
}