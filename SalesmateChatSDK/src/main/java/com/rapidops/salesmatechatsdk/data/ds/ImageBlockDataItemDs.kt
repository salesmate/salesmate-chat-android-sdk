package com.rapidops.salesmatechatsdk.data.ds

import com.google.gson.JsonDeserializationContext
import com.google.gson.JsonDeserializer
import com.google.gson.JsonElement
import com.rapidops.salesmatechatsdk.data.utils.GsonUtils
import com.rapidops.salesmatechatsdk.data.utils.getJsonObject
import com.rapidops.salesmatechatsdk.domain.models.message.BlockDataItem
import com.rapidops.salesmatechatsdk.domain.models.message.FileAttachmentData
import com.rapidops.salesmatechatsdk.domain.models.message.ImageBlockDataItem
import java.lang.reflect.Type

internal class ImageBlockDataItemDs : JsonDeserializer<BlockDataItem> {
    override fun deserialize(
        json: JsonElement,
        typeOfT: Type,
        context: JsonDeserializationContext
    ): BlockDataItem {
        val gson = GsonUtils.gson

        val imageBlockDataItem= ImageBlockDataItem()

        val jsonObject = json.asJsonObject

        val blockDataItem = gson.fromJson(jsonObject, BlockDataItem::class.java)
        imageBlockDataItem.cannedResponseId = blockDataItem.cannedResponseId
        imageBlockDataItem.orderedNo = blockDataItem.orderedNo
        imageBlockDataItem.blockType = blockDataItem.blockType
        imageBlockDataItem.isDraft = blockDataItem.isDraft
        imageBlockDataItem.fileId = blockDataItem.fileId
        imageBlockDataItem.messageId = blockDataItem.messageId
        imageBlockDataItem.id = blockDataItem.id
        imageBlockDataItem.body = blockDataItem.body
        imageBlockDataItem.linkName = blockDataItem.linkName

        jsonObject.getJsonObject("fileAttachmentData")?.let {
            imageBlockDataItem.fileAttachmentData = gson.fromJson(it, FileAttachmentData::class.java)
        }

        return imageBlockDataItem
    }
}