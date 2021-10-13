package com.rapidops.salesmatechatsdk.data.ds

import com.google.gson.JsonDeserializationContext
import com.google.gson.JsonDeserializer
import com.google.gson.JsonElement
import com.rapidops.salesmatechatsdk.data.utils.getString
import com.rapidops.salesmatechatsdk.domain.models.message.SourceMeta
import java.lang.reflect.Type

internal class SourceMetaDs : JsonDeserializer<SourceMeta> {
    override fun deserialize(
        json: JsonElement,
        typeOfT: Type,
        context: JsonDeserializationContext
    ): SourceMeta {

        val sourceMeta = SourceMeta()

        val jsonObject = json.asJsonObject

        sourceMeta.channelType = jsonObject.getString("channel_type") ?: ""
        sourceMeta.url = jsonObject.getString("url") ?: ""
        sourceMeta.userAgent = jsonObject.getString("user_agent") ?: ""

        return sourceMeta
    }
}