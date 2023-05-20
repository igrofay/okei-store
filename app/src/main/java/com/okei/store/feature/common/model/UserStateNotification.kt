package com.okei.store.feature.common.model

import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class UserStateNotification @Inject constructor() {
    private val subscribers : MutableMap<String, UserStateListener> = mutableMapOf()

    fun add(id: String, userStateListener: UserStateListener){
        subscribers[id] = userStateListener
    }

    fun remove(id: String){
        subscribers.remove(id)
    }

    fun changeState(state : State){
        for (listener in  subscribers.values){
            listener.onStateChange(state)
        }
    }


    interface UserStateListener{
        fun onStateChange(state : State)
    }
    enum class State{
        Authorized,
        NoLongerAuthorized,
        ErrorHasOccurred,
    }
}