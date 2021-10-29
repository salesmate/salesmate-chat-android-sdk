package com.rapidops.salesmatechatsdk.domain.models.message

import com.google.gson.annotations.SerializedName
import com.rapidops.salesmatechatsdk.data.reqmodels.Attachment
import com.rapidops.salesmatechatsdk.data.reqmodels.Blocks
import com.rapidops.salesmatechatsdk.data.reqmodels.SendMessageReq
import com.rapidops.salesmatechatsdk.domain.models.BaseModel
import com.rapidops.salesmatechatsdk.domain.models.BlockType
import com.rapidops.salesmatechatsdk.domain.models.User

internal data class MessageItem(

	@SerializedName("contact_name")
	var contactName: String = "",

	@SerializedName("unique_id")
	var uniqueId: String = "",

	@SerializedName("message_type")
	var messageType: String = "",

	@SerializedName("contact_id")
	var contactId: String = "",

	@SerializedName("verified_id")
	var verifiedId: Any? = null,

	@SerializedName("message_summary")
	var messageSummary: String = "",

	@SerializedName("blockData")
	var blockData: ArrayList<BlockDataItem> = arrayListOf(),

	@SerializedName("contact_email")
	var contactEmail: String = "",

	@SerializedName("deleted_date")
	var deletedDate: String = "",

	@SerializedName("is_internal_message")
	var isInternalMessage: Boolean = false,

	@SerializedName("referenced_teams")
	var referencedTeams: List<ReferenceTeam>? = null,

	@SerializedName("user_id")
	var userId: String = "",

	@SerializedName("referenced_users")
	var referencedUsers: List<ReferenceUser>? = null,

	@SerializedName("source_meta")
	var sourceMeta: SourceMeta? = null,

	@SerializedName("id")
	var id: String = "",

	@SerializedName("created_date")
	var createdDate: String = "",

	@SerializedName("linkname")
	var linkname: String = "",

	@SerializedName("is_bot")
	var isBot: Boolean = false,

	) : BaseModel() {

	var user: User? = null

	var conversationId: String = ""

	var sendStatus = SendStatus.NONE

	var isEmailSubmitted: Boolean = false

	val isStatusFailed: Boolean
		get() {
			return sendStatus == SendStatus.FAIL || sendStatus == SendStatus.UPLOADING_FAIL
		}
}


internal fun MessageItem.convertToSendMessageReq(): SendMessageReq {
	val sendMessageReq = this
	val messageItem = SendMessageReq().apply {
		this.messageType = sendMessageReq.messageType
		this.messageId = id
		this.isInbound = true
		this.isBot = sendMessageReq.isBot
		this.blockData.apply {
			sendMessageReq.blockData.forEach {
				add(it.convertToBlocks())
			}
		}
	}
	return messageItem
}

internal fun BlockDataItem.convertToBlocks(): Blocks {
	val blockItem = this
	val blockDataItem = when (blockItem) {
		is TextBlockDataItem -> {
			Blocks().apply {
				this.type = BlockType.TEXT.value
				this.text = blockItem.text
			}
		}
		is ImageBlockDataItem -> {
			Blocks().apply {
				this.type = BlockType.IMAGE.value
				this.attachment = blockItem.fileAttachmentData?.convertToAttachment()
			}
		}
		is FileBlockDataItem -> {
			Blocks().apply {
				this.type = BlockType.FILE.value
				this.attachment = blockItem.fileAttachmentData?.convertToAttachment()
			}
		}
		else -> {
			Blocks().apply {
				this.type = BlockType.TEXT.value
			}
		}
	}

	return blockDataItem
}

internal fun FileAttachmentData.convertToAttachment(): Attachment {
	val fileAttachmentData = this
	val attachment = Attachment().apply {
		this.contentType = fileAttachmentData.contentType
		this.gcpFileName = fileAttachmentData.gcpFileName
		this.gcpThumbnailFileName = fileAttachmentData.gcpThumbnailFileName
		this.name = fileAttachmentData.name
		this.thumbnail = fileAttachmentData.thumbnail
	}
	return attachment
}