package com.okei.store.feature.app.view

import androidx.activity.result.ActivityResultLauncher
import androidx.compose.runtime.compositionLocalOf
import com.vk.api.sdk.auth.VKScope

val LocalVkAuthLauncher = compositionLocalOf<ActivityResultLauncher<Collection<VKScope>>?>{ null }