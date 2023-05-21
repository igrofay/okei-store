package com.okei.store.feature.common.model

import java.util.UUID

class Box<T>(val value: T){
    private val uuid = UUID.randomUUID()

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false
        other as Box<*>
        return uuid == other.uuid
    }

    override fun hashCode(): Int {
        return uuid.hashCode() ?: 0
    }

}