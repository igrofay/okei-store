package com.okei.store.feature.app.view

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Modifier
import com.okei.store.feature.app.model.OkeiStoreVM
import com.okei.store.feature.common.theme.OkeiStoreTheme
import com.okei.store.feature.nav.view.AppNav
import com.vk.api.sdk.VK
import com.vk.api.sdk.auth.VKScope
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class OkeiStoreActivity : ComponentActivity() {
    private val okeiStoreVM : OkeiStoreVM by viewModels()
    private lateinit var vkAuthLauncher : ActivityResultLauncher<Collection<VKScope>>
    @SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        vkAuthLauncher = VK.login(this, okeiStoreVM::vkAuthenticationResult)
        setContent {
            CompositionLocalProvider(
                LocalVkAuthLauncher provides vkAuthLauncher
            ) {
                OkeiStoreTheme {
                    Scaffold(
                        modifier = Modifier.fillMaxSize(),
                        contentColor = MaterialTheme.colorScheme.background
                    ) {
                        AppNav()
                    }
                }
            }
        }
    }

}
