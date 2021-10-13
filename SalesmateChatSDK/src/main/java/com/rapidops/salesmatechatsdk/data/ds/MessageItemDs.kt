package com.rapidops.salesmatechatsdk.data.ds

import com.google.gson.JsonDeserializationContext
import com.google.gson.JsonDeserializer
import com.google.gson.JsonElement
import com.google.gson.reflect.TypeToken
import com.rapidops.salesmatechatsdk.data.utils.*
import com.rapidops.salesmatechatsdk.data.utils.GsonUtils
import com.rapidops.salesmatechatsdk.data.utils.getString
import com.rapidops.salesmatechatsdk.domain.models.User
import com.rapidops.salesmatechatsdk.domain.models.message.BlockDataItem
import com.rapidops.salesmatechatsdk.domain.models.message.MessageItem
import com.rapidops.salesmatechatsdk.domain.models.message.SourceMeta
import java.lang.reflect.Type

internal class MessageItemDs : JsonDeserializer<MessageItem> {
    override fun deserialize(
        json: JsonElement,
        typeOfT: Type,
        context: JsonDeserializationContext
    ): MessageItem {
        val gson = GsonUtils.gson

        val messageItem = MessageItem()

        val jsonObject = json.asJsonObject

        messageItem.contactName = jsonObject.getString("contact_name") ?: ""
        messageItem.uniqueId = jsonObject.getString("unique_id") ?: ""
        messageItem.messageType = jsonObject.getString("message_type") ?: ""
        messageItem.contactId = jsonObject.getString("contact_id") ?: ""
        messageItem.verifiedId = jsonObject.getString("verified_id") ?: ""
        messageItem.messageSummary = jsonObject.getString("message_summary") ?: ""

        jsonObject.getJsonArray("blockData")?.let {
            val listType = object : TypeToken<List<BlockDataItem>>() {}.type
            messageItem.blockData =gson.fromJson(it, listType)
        }

        messageItem.contactEmail = jsonObject.getString("contact_email") ?: ""
        messageItem.deletedDate = jsonObject.getString("deleted_date") ?: ""

        messageItem.isInternalMessage = jsonObject.getBoolean("is_internal_message")

        jsonObject.getJsonArray("referenced_teams")?.let {
            val listType = object : TypeToken<List<Any>>() {}.type
            messageItem.referencedTeams =gson.fromJson(it, listType)
        }

        messageItem.userId = jsonObject.getString("user_id") ?: ""


        jsonObject.getJsonArray("referenced_users")?.let {
            val listType = object : TypeToken<List<Any>>() {}.type
            messageItem.referencedUsers =gson.fromJson(it, listType)
        }

        jsonObject.getJsonObject("source_meta")?.let {
            messageItem.sourceMeta = GsonUtils.gson.fromJson(it, SourceMeta::class.java)
        }

        messageItem.id = jsonObject.getString("id") ?: ""
        messageItem.createdDate = jsonObject.getString("created_date") ?: ""
        messageItem.linkname = jsonObject.getString("linkname") ?: ""

        messageItem.isBot = jsonObject.getBoolean("is_bot")

        return messageItem
    }
}