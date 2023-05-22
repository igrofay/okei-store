package com.okei.store.feature.nav.view

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.ModalBottomSheetLayout
import androidx.compose.material.ModalBottomSheetValue
import androidx.compose.material.rememberModalBottomSheetState
import androidx.compose.material3.BottomSheetDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.contentColorFor
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.*
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.saveable.rememberSaveableStateHolder
import androidx.compose.ui.Alignment
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
import com.okei.store.feature.nav.model.MainContentRouting
import com.okei.store.feature.profile.view.ProfileScreen
import com.okei.store.feature.shop.view.ShopScreen
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

typealias SheetContent = @Composable () -> Unit
@OptIn(ExperimentalMaterial3Api::class, ExperimentalMaterialApi::class)
@Composable
fun MainContentNav(
    createOrder: () -> Unit,
) {
    val navController = rememberNavController()
    val localCoroutine = rememberCoroutineScope()
    var bottomSheetContent: SheetContent? by remember { mutableStateOf(null) }
    val sheetState = rememberModalBottomSheetState(
        initialValue = ModalBottomSheetValue.Hidden,
        confirmValueChange = {
            when(it){
                ModalBottomSheetValue.Hidden -> {
                    bottomSheetContent = null
                    true
                }
                else-> true
            }
        }
    )
    val showBottomSheet: (SheetContent) -> Unit = { content: SheetContent ->
        bottomSheetContent = content
        localCoroutine.launch { sheetState.show() }
    }
    val hideBottomSheet: () -> Unit = {
        localCoroutine.launch {
            sheetState.hide()
            bottomSheetContent = null
        }
    }
    ModalBottomSheetLayout(
        sheetContent = {
            BackHandler(sheetState.currentValue != ModalBottomSheetValue.Hidden) {
                hideBottomSheet.invoke()
            }
            Column(
                modifier = Modifier.fillMaxWidth(),
            ) {
                Spacer(
                    modifier = Modifier
                        .align(Alignment.CenterHorizontally)
                        .padding(vertical = 14.dp)
                        .height(6.dp)
                        .width(40.dp)
                        .background(
                            MaterialTheme.colorScheme.onBackground.copy(0.5f),
                            CircleShape
                        )
                )
                bottomSheetContent?.invoke()
            }

        },
        sheetState = sheetState,
        sheetBackgroundColor = BottomSheetDefaults.ContainerColor,
        sheetContentColor = contentColorFor(BottomSheetDefaults.ContainerColor),
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
                    ShopScreen(
                        showBottomSheet = showBottomSheet,
                        hideBottomSheet = hideBottomSheet,
                    )
                }
                composable(MainContentRouting.Cart.route) {
                    CartScreen(
                        createOrder = createOrder,
                        showBottomSheet = showBottomSheet,
                        hideBottomSheet = hideBottomSheet,
                    )
                }
                composable(MainContentRouting.Profile.route) {
                    ProfileScreen()
                }
            }
        }
    }
}