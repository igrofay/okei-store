package com.okei.store.data.data_source.command

import com.okei.store.data.model.VKUser
import com.vk.api.sdk.VKApiJSONResponseParser
import com.vk.api.sdk.VKApiManager
import com.vk.api.sdk.VKMethodCall
import com.vk.api.sdk.internal.ApiCommand
import org.json.JSONObject

class VKPhotoUserCommand(private val typeSize: String): ApiCommand<String?>() {
    override fun onExecute(manager: VKApiManager): String? {
        val call = VKMethodCall.Builder()
            .method("photos.get")
            .version(manager.config.version)
            .args("album_id", "profile")
            .args("count", 1)
            .args("rev", 1)
            .build()
        return manager.execute(call, VKPhotoParser(typeSize))
    }

    private class VKPhotoParser(val typeSize: String) : VKApiJSONResponseParser<String?> {
        override fun parse(responseJson: JSONObject): String? {
            val response = responseJson
                .getJSONObject("response")
            val listSizesImage = if(response.getJSONArray("items").length() != 0){
                response
                    .getJSONArray("items")
                    .getJSONObject(0)
                    .getJSONArray("sizes")
            }else{
                return null
            }
            var imageUrl: String? = null
            for (index in 0 until listSizesImage.length()){
                if(listSizesImage
                    .getJSONObject(index)
                    .getString("type") == typeSize){
                    imageUrl = listSizesImage
                        .getJSONObject(index)
                        .getString("url")
                    break
                }
            }
            return imageUrl
        }

    }

}