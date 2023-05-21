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
        val call = VKMethodCall.Builder()
            .method("account.getProfileInfo")
            .version(manager.config.version)
            .build()
        return manager.execute(call, VKUserParser())
    }
    private class VKUserParser : VKApiJSONResponseParser<VKUser>{
        override fun parse(responseJson: JSONObject): VKUser {
            val response = responseJson.getJSONObject("response")
            return VKUser(
                firstName = response.getString("first_name"),
                lastName = response.getString("last_name"),
                phone = response.getString("phone")
            )
        }

    }
}