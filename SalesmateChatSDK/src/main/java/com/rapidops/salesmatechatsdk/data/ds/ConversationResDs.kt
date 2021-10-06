package com.rapidops.salesmatechatsdk.data.ds

import com.google.gson.JsonDeserializationContext
import com.google.gson.JsonDeserializer
import com.google.gson.JsonElement
import com.google.gson.reflect.TypeToken
import com.rapidops.salesmatechatsdk.data.resmodels.ConversationRes
import com.rapidops.salesmatechatsdk.data.utils.GsonUtils
import com.rapidops.salesmatechatsdk.data.utils.getJsonArray
import com.rapidops.salesmatechatsdk.domain.models.Conversations
import java.lang.reflect.Type

internal class ConversationResDs : JsonDeserializer<ConversationRes> {
    override fun deserialize(
        json: JsonElement,
        typeOfT: Type,
        context: JsonDeserializationContext
    ): ConversationRes {
        val gson = GsonUtils.gson
        val conversationRes = ConversationRes()

        val jsonObject = json.asJsonObject

        jsonObject.getJsonArray("Data")?.let {

            val listType = object : TypeToken<List<Conversations>>() {}.type
            conversationRes.conversationList = gson.fromJson(it, listType)
        }
        return conversationRes
    }
}