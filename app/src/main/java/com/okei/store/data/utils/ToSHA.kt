package com.okei.store.data.utils

import java.security.MessageDigest

object ToSHA {
    private val md = MessageDigest.getInstance("SHA-256")
    fun toSHA256(text: String) : String{
        val byteArray = md.digest(text.toByteArray())
        return byteArray.joinToString("") { "%02x".format(it) }
    }
    fun toSHA256(number: Long) : String{
        val byteArray = md.digest(number.toString().toByteArray())
        return byteArray.joinToString("") { "%02x".format(it) }
    }
}