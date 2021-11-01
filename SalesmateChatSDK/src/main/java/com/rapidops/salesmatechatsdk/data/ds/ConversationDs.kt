package com.rapidops.salesmatechatsdk.data.ds

import com.google.gson.JsonDeserializationContext
import com.google.gson.JsonDeserializer
import com.google.gson.JsonElement
import com.rapidops.salesmatechatsdk.data.utils.GsonUtils
import com.rapidops.salesmatechatsdk.data.utils.getBoolean
import com.rapidops.salesmatechatsdk.data.utils.getJsonObject
import com.rapidops.salesmatechatsdk.data.utils.getString
import com.rapidops.salesmatechatsdk.domain.models.Conversations
import com.rapidops.salesmatechatsdk.domain.models.LastMessageData
import java.lang.reflect.Type

internal class ConversationDs : JsonDeserializer<Conversations> {
    override fun deserialize(
        json: JsonElement,
        typeOfT: Type,
        context: JsonDeserializationContext
    ): Conversations {
        val gson = GsonUtils.gson

        val conversations = Conversations()

        val jsonObject = json.asJsonObject

        conversations.lastMessageDate = jsonObject.getString("last_message_date") ?: ""
        conversations.uniqueId = jsonObject.getString("unique_id") ?: ""
        conversations.sessionId = jsonObject.getString("session_id") ?: ""
        conversations.contactId = jsonObject.getString("contact_id") ?: ""
        conversations.verifiedId = jsonObject.getString("verified_id") ?: ""
        conversations.lastParticipatingUserId =
            jsonObject.getString("last_participating_user_id") ?: ""
        conversations.brandId = jsonObject.getString("brand_id") ?: ""
        conversations.ownerUser = jsonObject.getString("owner_user") ?: ""
        conversations.contactHasRead = jsonObject.getBoolean("contact_has_read")

        jsonObject.getJsonObject("lastMessageData")?.let {
            conversations.lastMessageData = gson.fromJson(it, LastMessageData::class.java)
        }

        conversations.name = jsonObject.getString("name") ?: ""
        conversations.id = jsonObject.getString("id") ?: ""
        conversations.createdDate = jsonObject.getString("created_date") ?: ""
        conversations.email = jsonObject.getString("email") ?: ""
        conversations.status = jsonObject.getString("status") ?: ""
        conversations.closedDate = jsonObject.getString("closedDate") ?: ""
        conversations.rating = jsonObject.getString("rating") ?: ""
        conversations.remark = jsonObject.getString("remark") ?: ""
        conversations.userHasRead = jsonObject.getBoolean("userHasRead")

        return conversations
    }
}