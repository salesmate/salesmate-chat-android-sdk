package com.rapidops.salesmatechatsdk.data.ds

import com.google.gson.JsonDeserializationContext
import com.google.gson.JsonDeserializer
import com.google.gson.JsonElement
import com.rapidops.salesmatechatsdk.data.utils.GsonUtils
import com.rapidops.salesmatechatsdk.data.utils.getString
import com.rapidops.salesmatechatsdk.domain.models.events.ChatNewMessage
import java.lang.reflect.Type

internal class ChatNewMessageDs : JsonDeserializer<ChatNewMessage> {
    override fun deserialize(
        json: JsonElement,
        typeOfT: Type,
        context: JsonDeserializationContext
    ): ChatNewMessage {
        val gson = GsonUtils.gson

        val chatNewMessage = ChatNewMessage()

        val jsonObject = json.asJsonObject

        chatNewMessage.conversationId = jsonObject.getString("conversationId") ?: ""
        chatNewMessage.createdDate = jsonObject.getString("createdDate") ?: ""
        chatNewMessage.messageId = jsonObject.getString("messageId") ?: ""


        return chatNewMessage
    }
}