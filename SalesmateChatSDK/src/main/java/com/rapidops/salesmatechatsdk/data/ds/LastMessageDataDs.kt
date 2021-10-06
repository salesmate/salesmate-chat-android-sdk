package com.rapidops.salesmatechatsdk.data.ds

import com.google.gson.JsonDeserializationContext
import com.google.gson.JsonDeserializer
import com.google.gson.JsonElement
import com.rapidops.salesmatechatsdk.data.utils.getString
import com.rapidops.salesmatechatsdk.domain.models.LastMessageData
import java.lang.reflect.Type

internal class LastMessageDataDs : JsonDeserializer<LastMessageData> {
    override fun deserialize(
        json: JsonElement,
        typeOfT: Type,
        context: JsonDeserializationContext
    ): LastMessageData {

        val lastMessageData = LastMessageData()

        val jsonObject = json.asJsonObject

        lastMessageData.uniqueId = jsonObject.getString("unique_id") ?: ""
        lastMessageData.userId = jsonObject.getString("user_id") ?: ""
        lastMessageData.conversationId = jsonObject.getString("conversation_id") ?: ""
        lastMessageData.messageType = jsonObject.getString("message_type") ?: ""
        lastMessageData.id = jsonObject.getString("id") ?: ""
        lastMessageData.contactId = jsonObject.getString("contact_id") ?: ""
        lastMessageData.verifiedId = jsonObject.getString("verified_id") ?: ""
        lastMessageData.messageSummary = jsonObject.getString("message_summary") ?: ""

        return lastMessageData
    }
}