package com.rapidops.salesmatechatsdk.data.ds

import com.google.gson.JsonDeserializationContext
import com.google.gson.JsonDeserializer
import com.google.gson.JsonElement
import com.rapidops.salesmatechatsdk.data.utils.*
import com.rapidops.salesmatechatsdk.domain.models.message.BlockDataItem
import com.rapidops.salesmatechatsdk.domain.models.message.FileAttachmentData
import java.lang.reflect.Type

internal class BlockDataItemDs : JsonDeserializer<BlockDataItem> {
    override fun deserialize(
        json: JsonElement,
        typeOfT: Type,
        context: JsonDeserializationContext
    ): BlockDataItem {
        val gson = GsonUtils.gson

        val blockDataItem = BlockDataItem()

        val jsonObject = json.asJsonObject

        blockDataItem.cannedResponseId = jsonObject.getString("canned_response_id") ?: ""
        blockDataItem.orderedNo = jsonObject.getInt("ordered_no")
        blockDataItem.blockType = jsonObject.getString("block_type") ?: ""

        jsonObject.getJsonObject("fileAttachmentData")?.let {
            blockDataItem.fileAttachmentData = gson.fromJson(it, FileAttachmentData::class.java)
        }

        blockDataItem.isDraft = jsonObject.getBoolean("is_draft")
        blockDataItem.fileId = jsonObject.getString("file_id") ?: ""
        blockDataItem.messageId = jsonObject.getString("message_id") ?: ""
        blockDataItem.id = jsonObject.getString("id") ?: ""
        blockDataItem.text = jsonObject.getString("text") ?: ""
        blockDataItem.body = jsonObject.getString("body") ?: ""
        blockDataItem.linkName = jsonObject.getString("linkName") ?: ""

        return blockDataItem
    }
}