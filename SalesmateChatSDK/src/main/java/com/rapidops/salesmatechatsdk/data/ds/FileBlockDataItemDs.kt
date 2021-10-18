package com.rapidops.salesmatechatsdk.data.ds

import com.google.gson.JsonDeserializationContext
import com.google.gson.JsonDeserializer
import com.google.gson.JsonElement
import com.rapidops.salesmatechatsdk.data.utils.*
import com.rapidops.salesmatechatsdk.domain.models.BlockType
import com.rapidops.salesmatechatsdk.domain.models.message.BlockDataItem
import com.rapidops.salesmatechatsdk.domain.models.message.FileAttachmentData
import com.rapidops.salesmatechatsdk.domain.models.message.FileBlockDataItem
import com.rapidops.salesmatechatsdk.domain.models.message.TextBlockDataItem
import java.lang.reflect.Type

internal class FileBlockDataItemDs : JsonDeserializer<BlockDataItem> {
    override fun deserialize(
        json: JsonElement,
        typeOfT: Type,
        context: JsonDeserializationContext
    ): BlockDataItem {
        val gson = GsonUtils.gson

        val fileBlockDataItem= FileBlockDataItem()

        val jsonObject = json.asJsonObject

        val blockDataItem = gson.fromJson(jsonObject, BlockDataItem::class.java)
        fileBlockDataItem.cannedResponseId = blockDataItem.cannedResponseId
        fileBlockDataItem.orderedNo = blockDataItem.orderedNo
        fileBlockDataItem.blockType = blockDataItem.blockType
        fileBlockDataItem.isDraft = blockDataItem.isDraft
        fileBlockDataItem.fileId = blockDataItem.fileId
        fileBlockDataItem.messageId = blockDataItem.messageId
        fileBlockDataItem.id = blockDataItem.id
        fileBlockDataItem.body = blockDataItem.body
        fileBlockDataItem.linkName = blockDataItem.linkName

        jsonObject.getJsonObject("fileAttachmentData")?.let {
            fileBlockDataItem.fileAttachmentData = gson.fromJson(it, FileAttachmentData::class.java)
        }

        return fileBlockDataItem
    }
}