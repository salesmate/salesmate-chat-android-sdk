package com.rapidops.salesmatechatsdk.data.ds

import com.google.gson.JsonDeserializationContext
import com.google.gson.JsonDeserializer
import com.google.gson.JsonElement
import com.rapidops.salesmatechatsdk.data.utils.GsonUtils
import com.rapidops.salesmatechatsdk.data.utils.getString
import com.rapidops.salesmatechatsdk.domain.models.message.BlockDataItem
import com.rapidops.salesmatechatsdk.domain.models.message.TextBlockDataItem
import java.lang.reflect.Type

internal class TextBlockDataItemDs : JsonDeserializer<TextBlockDataItem> {
    override fun deserialize(
        json: JsonElement,
        typeOfT: Type,
        context: JsonDeserializationContext
    ): TextBlockDataItem {
        val gson = GsonUtils.gson

        val textBlockDataItem = TextBlockDataItem()

        val jsonObject = json.asJsonObject

        val blockDataItem = gson.fromJson(jsonObject, BlockDataItem::class.java)
        textBlockDataItem.cannedResponseId = blockDataItem.cannedResponseId
        textBlockDataItem.orderedNo = blockDataItem.orderedNo
        textBlockDataItem.blockType = blockDataItem.blockType
        textBlockDataItem.isDraft = blockDataItem.isDraft
        textBlockDataItem.fileId = blockDataItem.fileId
        textBlockDataItem.messageId = blockDataItem.messageId
        textBlockDataItem.id = blockDataItem.id
        textBlockDataItem.body = blockDataItem.body
        textBlockDataItem.linkName = blockDataItem.linkName


        textBlockDataItem.text = jsonObject.getString("text") ?: ""

        return textBlockDataItem
    }
}