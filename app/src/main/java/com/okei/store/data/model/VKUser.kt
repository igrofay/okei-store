package com.okei.store.data.model

import com.okei.store.domain.model.user.UserModel


data class VKUser(
    val firstName: String,
    val lastName : String,
    val phone: String,
){
    fun fromVKUserToUserModel(image: String?) = object : UserModel{
        override val name = "$firstName $lastName"
        override val phone = this@VKUser.phone
        override val imageUrl = image
    }
}