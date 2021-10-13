package com.rapidops.salesmatechatsdk.data.ds

import com.google.gson.JsonDeserializationContext
import com.google.gson.JsonDeserializer
import com.google.gson.JsonElement
import com.google.gson.reflect.TypeToken
import com.rapidops.salesmatechatsdk.data.resmodels.MessageListRes
import com.rapidops.salesmatechatsdk.data.utils.GsonUtils
import com.rapidops.salesmatechatsdk.data.utils.getJsonArray
import com.rapidops.salesmatechatsdk.domain.models.message.MessageItem
import java.lang.reflect.Type

internal class MessageListResDs : JsonDeserializer<MessageListRes> {
    override fun deserialize(
        json: JsonElement,
        typeOfT: Type,
        context: JsonDeserializationContext
    ): MessageListRes {
        val gson = GsonUtils.gson
        val messageListRes = MessageListRes()

        val jsonObject = json.asJsonObject

        jsonObject.getJsonArray("Data")?.let {

            val listType = object : TypeToken<List<MessageItem>>() {}.type
            messageListRes.messageList = gson.fromJson(it, listType)
        }
        return messageListRes
    }
}