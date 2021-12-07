package com.rapidops.salesmatechatsdk.data.ds

import com.google.gson.JsonDeserializationContext
import com.google.gson.JsonDeserializer
import com.google.gson.JsonElement
import com.google.gson.reflect.TypeToken
import com.rapidops.salesmatechatsdk.data.utils.*
import com.rapidops.salesmatechatsdk.domain.models.BlockType
import com.rapidops.salesmatechatsdk.domain.models.message.*
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

        messageItem.contactEmail = jsonObject.getString("contact_email") ?: ""
        messageItem.deletedDate = jsonObject.getString("deleted_date") ?: ""

        messageItem.isInternalMessage = jsonObject.getBoolean("is_internal_message")

        jsonObject.getJsonArray("referenced_teams")?.let {
            val listType = object : TypeToken<List<ReferenceTeam>>() {}.type
            messageItem.referencedTeams =gson.fromJson(it, listType)
        }

        messageItem.userId = jsonObject.getString("user_id") ?: ""


        jsonObject.getJsonArray("referenced_users")?.let {
            val listType = object : TypeToken<List<ReferenceUser>>() {}.type
            messageItem.referencedUsers =gson.fromJson(it, listType)
        }

        jsonObject.getJsonObject("source_meta")?.let {
            messageItem.sourceMeta = GsonUtils.gson.fromJson(it, SourceMeta::class.java)
        }

        messageItem.id = jsonObject.getString("id") ?: ""
        messageItem.createdDate = jsonObject.getString("created_date") ?: ""
        messageItem.linkname = jsonObject.getString("linkname") ?: ""

        messageItem.isBot = jsonObject.getBoolean("is_bot")

        messageItem.conversationId = jsonObject.getString("conversation_id") ?: ""

        val blockDataList = arrayListOf<BlockDataItem>()
        if (messageItem.deletedDate.isNotEmpty()) {
            val blockDataItem = DeleteBlockDataItem()
            blockDataItem.text = messageItem.messageSummary
            blockDataItem.isSelfMessage = messageItem.userId.isEmpty()
            blockDataList.add(blockDataItem)
        }

        jsonObject.getJsonArray("blockData")?.let {
            /*val listType = object : TypeToken<List<BlockDataItem>>() {}.type
            messageItem.blockData =gson.fromJson(it, listType)*/

            it.forEach { jsonElement ->
                val blockDataJson = jsonElement.asJsonObject
                if (blockDataJson.hasProperty("block_type")) {
                    val blockTypeStr = blockDataJson.getString("block_type") ?: ""
                    val blockType = BlockType.findEnumFromValue(blockTypeStr)
                    var blockDataItem = BlockDataItem()
                    if (blockType == BlockType.TEXT) {
                        blockDataItem =
                            gson.fromJson(jsonElement, TextBlockDataItem::class.java)
                    } else if (blockType == BlockType.IMAGE) {
                        blockDataItem =
                            gson.fromJson(jsonElement, ImageBlockDataItem::class.java)
                    } else if (blockType == BlockType.FILE) {
                        blockDataItem =
                            gson.fromJson(jsonElement, FileBlockDataItem::class.java)
                    } else if (blockType == BlockType.HTML || blockType == BlockType.ORDERED_LIST || blockType == BlockType.UNORDERED_LIST) {
                        blockDataItem =
                            gson.fromJson(jsonElement, HtmlBlockDataItem::class.java)
                    }
                    blockDataItem.blockType = blockType
                    blockDataItem.isSelfMessage =
                        messageItem.userId.isEmpty() && messageItem.isBot.not()
                    blockDataList.add(blockDataItem)
                }
            }
        }
        messageItem.blockData = blockDataList

        return messageItem
    }
}