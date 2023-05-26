package com.okei.store.feature.common.model

import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class CreateOrderNotification @Inject constructor() {
    private val subscribers : MutableMap<String, Listener> = mutableMapOf()

    fun add(id: String, userStateListener: Listener){
        subscribers[id] = userStateListener
    }

    fun remove(id: String){
        subscribers.remove(id)
    }

    fun notifyAboutCreatedProduct(){
        for (listener in subscribers.values){
            listener.productHasBeenCreated()
        }
    }
    interface Listener{
        fun productHasBeenCreated()
    }
}