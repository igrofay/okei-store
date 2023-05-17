package com.okei.store.feature.nav.view

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.BottomSheetScaffold
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SheetValue
import androidx.compose.material3.Text
import androidx.compose.material3.rememberBottomSheetScaffoldState
import androidx.compose.material3.rememberStandardBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.compositionLocalOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.okei.store.feature.cart.view.CartScreen
import com.okei.store.feature.nav.model.BottomSheetManager
import com.okei.store.feature.nav.model.MainContentRouting
import com.okei.store.feature.profile.view.ProfileScreen
import com.okei.store.feature.shop.view.ShopScreen
import kotlinx.coroutines.launch

val LocalBottomSheetManager = compositionLocalOf<BottomSheetManager>{ error("Not found") }

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainContentNav(
    createOrder: ()->Unit,
) {
    val navController = rememberNavController()
    val scaffoldState = rememberBottomSheetScaffoldState(
        rememberStandardBottomSheetState(
            initialValue = SheetValue.Hidden,
            skipHiddenState = false
        )
    )
    CompositionLocalProvider(LocalBottomSheetManager provides BottomSheetManager(scaffoldState.bottomSheetState)){
        BottomSheetScaffold(
            scaffoldState = scaffoldState,
            sheetContent = {
                val localCoroutine = rememberCoroutineScope()
                val manager = LocalBottomSheetManager.current
                BackHandler(
                    scaffoldState.bottomSheetState.currentValue != SheetValue.Hidden
                ) {
                    localCoroutine.launch {
                        manager.close()
                    }
                }
                val content by manager.content
                content.invoke()
            },
            sheetPeekHeight = 0.dp
        ) {
            Scaffold(
                bottomBar = {
                    NavigationBar(modifier = Modifier.fillMaxWidth()) {
                        val navBackStackEntry by navController.currentBackStackEntryAsState()
                        val currentDestination = navBackStackEntry?.destination
                        MainContentRouting.listRouting.forEach { replyDestination ->
                            NavigationBarItem(
                                selected = currentDestination?.hierarchy?.any { it.route == replyDestination.route } == true,
                                onClick = {
                                    navController.navigate(replyDestination.route) {
                                        popUpTo(navController.graph.findStartDestination().id) {
                                            saveState = true
                                        }
                                        launchSingleTop = true
                                        restoreState = true
                                    }
                                },
                                icon = {
                                    Icon(
                                        painterResource(replyDestination.icon),
                                        contentDescription = null
                                    )
                                },
                                label = {
                                    Text(text = stringResource(replyDestination.header))
                                }
                            )
                        }
                    }
                },
            ) { innerPadding ->
                NavHost(
                    navController,
                    startDestination = MainContentRouting.ProductList.route,
                    Modifier.padding(innerPadding)
                ) {
                    composable(MainContentRouting.ProductList.route) {
                        val bottomSheetManager = LocalBottomSheetManager.current
                        val coroutineScope = rememberCoroutineScope()
                        BackHandler(scaffoldState.bottomSheetState.currentValue != SheetValue.Hidden) {
                            coroutineScope.launch {
                                bottomSheetManager.close()
                            }
                        }
                        ShopScreen()
                    }
                    composable(MainContentRouting.Cart.route) {
                        val bottomSheetManager = LocalBottomSheetManager.current
                        val coroutineScope = rememberCoroutineScope()
                        BackHandler(scaffoldState.bottomSheetState.currentValue != SheetValue.Hidden) {
                            coroutineScope.launch {
                                bottomSheetManager.close()
                            }
                        }
                        CartScreen(
                            createOrder = createOrder
                        )
                    }
                    composable(MainContentRouting.Profile.route) {
                        ProfileScreen()
                    }
                }
            }
        }
    }

}