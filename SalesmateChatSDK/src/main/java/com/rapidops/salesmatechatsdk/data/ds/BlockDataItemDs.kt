package com.rapidops.salesmatechatsdk.data.ds

import com.google.gson.JsonDeserializationContext
import com.google.gson.JsonDeserializer
import com.google.gson.JsonElement
import com.rapidops.salesmatechatsdk.data.utils.GsonUtils
import com.rapidops.salesmatechatsdk.data.utils.getBoolean
import com.rapidops.salesmatechatsdk.data.utils.getInt
import com.rapidops.salesmatechatsdk.data.utils.getString
import com.rapidops.salesmatechatsdk.domain.models.BlockType
import com.rapidops.salesmatechatsdk.domain.models.message.BlockDataItem
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
        blockDataItem.blockType =
            BlockType.findEnumFromValue(jsonObject.getString("block_type") ?: "")

        blockDataItem.isDraft = jsonObject.getBoolean("is_draft")
        blockDataItem.fileId = jsonObject.getString("file_id") ?: ""
        blockDataItem.messageId = jsonObject.getString("message_id") ?: ""
        blockDataItem.id = jsonObject.getString("id") ?: ""
        blockDataItem.body = jsonObject.getString("body") ?: ""
        blockDataItem.linkName = jsonObject.getString("linkName") ?: ""

        return blockDataItem
    }
}