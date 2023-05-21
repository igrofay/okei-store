package com.okei.store.data.data_source.api.vk

import com.okei.store.data.data_source.command.VKPhotoUserCommand
import com.okei.store.data.data_source.command.VKUserCommand
import com.vk.api.sdk.VK
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class VKApi @Inject constructor() {
    fun getProfile() = VK.executeSync(VKUserCommand())
    fun getPhotoProfile() = VK.executeSync(VKPhotoUserCommand("x"))
}