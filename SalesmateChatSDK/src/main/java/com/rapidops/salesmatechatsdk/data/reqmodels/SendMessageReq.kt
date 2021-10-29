package com.rapidops.salesmatechatsdk.data.reqmodels

import com.google.gson.annotations.SerializedName
import com.rapidops.salesmatechatsdk.domain.models.BlockType
import com.rapidops.salesmatechatsdk.domain.models.message.*

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

    @SerializedName("email")
    var email: String? = null

}

internal class Blocks {
    @SerializedName("type")
    var type: String = ""

    @SerializedName("text")
    var text: String? = null

    @SerializedName("attachment")
    var attachment: Attachment? = null

}

internal class Attachment {

    @SerializedName("content_type")
    var contentType: String = ""

    @SerializedName("gcp_file_name")
    var gcpFileName: String = ""

    @SerializedName("gcp_thumbnail_file_name")
    var gcpThumbnailFileName: String? = null

    @SerializedName("name")
    var name: String = ""

    @SerializedName("thumbnail")
    var thumbnail: String? = null

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
    } else if (blockItem.type == BlockType.IMAGE.value) {
        ImageBlockDataItem().apply {
            this.blockType = BlockType.IMAGE
            this.fileAttachmentData = blockItem.attachment?.convertToFileAttachment()
            this.isSelfMessage = true
        }
    } else if (blockItem.type == BlockType.FILE.value) {
        FileBlockDataItem().apply {
            this.blockType = BlockType.FILE
            this.fileAttachmentData = blockItem.attachment?.convertToFileAttachment()
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

internal fun Attachment.convertToFileAttachment(): FileAttachmentData {
    val attachment = this
    val fileAttachmentData = FileAttachmentData().apply {
        this.contentType = attachment.contentType
        this.gcpFileName = attachment.gcpFileName
        this.gcpThumbnailFileName = attachment.gcpThumbnailFileName
        this.name = attachment.name
        this.thumbnail = attachment.thumbnail
    }
    return fileAttachmentData
}