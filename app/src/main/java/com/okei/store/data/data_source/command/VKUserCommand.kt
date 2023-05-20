package com.okei.store.data.data_source.command

import android.util.Log
import com.okei.store.data.model.VKUser
import com.vk.api.sdk.VK
import com.vk.api.sdk.VKApiJSONResponseParser
import com.vk.api.sdk.VKApiManager
import com.vk.api.sdk.VKMethodCall
import com.vk.api.sdk.internal.ApiCommand
import org.json.JSONObject


class VKUserCommand : ApiCommand<VKUser>() {
    override fun onExecute(manager: VKApiManager): VKUser {
        Log.e("VKUserCommand", "1")
        val call = VKMethodCall.Builder()
            .method("account.getProfileNavigationInfo")
            .version(manager.config.version)
            .args("client_id", VK.getUserId().value)
            .build()
        return manager.execute(call, VKUserParser())
    }
    private class VKUserParser : VKApiJSONResponseParser<VKUser>{
        override fun parse(responseJson: JSONObject): VKUser {
            val response = responseJson.getJSONObject("response")
            Log.e("VKUserParser", response.names()?.join(",").toString())
            return VKUser(
                image = response.getString("photo_200"),
                firstName = response.getString("first_name"),
                lastName = response.getString("last_name"),
                phone = response.getString("phone")
            )
        }

    }
}