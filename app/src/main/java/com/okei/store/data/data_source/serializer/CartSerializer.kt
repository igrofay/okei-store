package com.okei.store.data.data_source.serializer

import androidx.datastore.core.Serializer
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import java.io.InputStream
import java.io.OutputStream


object CartSerializer : Serializer<Map<String, Int>> {
    const val cartFileName = "cart.preferences_pb"
    override val defaultValue: Map<String, Int>
        get() = mapOf()

    override suspend fun readFrom(input: InputStream): Map<String, Int> {
        return Json.decodeFromString(input.readBytes().decodeToString())
    }

    override suspend fun writeTo(t: Map<String, Int>, output: OutputStream) {
        withContext(Dispatchers.IO){
            output.write(
                Json.encodeToString(t)
                    .encodeToByteArray()
            )
        }
    }
}