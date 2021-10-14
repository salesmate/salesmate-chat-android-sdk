package com.rapidops.salesmatechatsdk.data.ds

import com.google.gson.JsonDeserializationContext
import com.google.gson.JsonDeserializer
import com.google.gson.JsonElement
import com.rapidops.salesmatechatsdk.data.resmodels.ConversationDetailRes
import com.rapidops.salesmatechatsdk.data.utils.GsonUtils
import com.rapidops.salesmatechatsdk.data.utils.getJsonArray
import com.rapidops.salesmatechatsdk.data.utils.getJsonObject
import com.rapidops.salesmatechatsdk.domain.models.Conversations
import java.lang.reflect.Type

internal class ConversationDetailResDs : JsonDeserializer<ConversationDetailRes> {
    override fun deserialize(
        json: JsonElement,
        typeOfT: Type,
        context: JsonDeserializationContext
    ): ConversationDetailRes {
        val gson = GsonUtils.gson
        val conversationDetailRes = ConversationDetailRes()

        val jsonObject = json.asJsonObject

        jsonObject.getJsonObject("Data")?.let {
            conversationDetailRes.conversations = gson.fromJson(it, Conversations::class.java)
        }
        return conversationDetailRes
    }
}