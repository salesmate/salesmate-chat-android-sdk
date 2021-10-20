package com.rapidops.salesmatechatsdk.data.reqmodels

import com.google.gson.annotations.SerializedName
import com.rapidops.salesmatechatsdk.domain.models.BlockType
import com.rapidops.salesmatechatsdk.domain.models.message.BlockDataItem
import com.rapidops.salesmatechatsdk.domain.models.message.MessageItem
import com.rapidops.salesmatechatsdk.domain.models.message.TextBlockDataItem

internal class SendMessageReq {

    @SerializedName("blocks")
    var blockData: ArrayList<Blocks> = arrayListOf()

    @SerializedName("message_type")
    var messageType: String = ""

    @SerializedName("message_id")
    var messageId: String = ""

    @SerializedName("is_bot")
    var isBot: Boolean = false

    @SerializedName("is_inbound")
    var isInbound: Boolean = false

    @SerializedName("conversation_name")
    var conversationName: String = ""

}

internal class Blocks {
    @SerializedName("type")
    var type: String = ""

    @SerializedName("text")
    var text: String = ""

}

internal fun SendMessageReq.convertToMessageItem(): MessageItem {
    val sendMessageReq = this
    val messageItem = MessageItem().apply {
        this.messageType = sendMessageReq.messageType
        this.id = messageId
        this.isBot = sendMessageReq.isBot
        this.blockData.apply {
            sendMessageReq.blockData.forEach {
                add(it.convertToBlockDataItem())
            }
        }
    }
    return messageItem
}

internal fun Blocks.convertToBlockDataItem(): BlockDataItem {
    val blockItem = this
    val blockDataItem = if (blockItem.type == BlockType.TEXT.value) {
        TextBlockDataItem().apply {
            this.blockType = BlockType.TEXT
            this.text = blockItem.text
            this.isSelfMessage = true
        }
    } else {
        TextBlockDataItem().apply {
            this.blockType = BlockType.TEXT
            this.text = blockItem.text
            this.isSelfMessage = true
        }
    }

    return blockDataItem
}