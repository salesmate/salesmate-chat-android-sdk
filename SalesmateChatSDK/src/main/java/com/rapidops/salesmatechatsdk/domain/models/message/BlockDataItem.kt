package com.rapidops.salesmatechatsdk.domain.models.message

import com.google.gson.annotations.SerializedName
import com.rapidops.salesmatechatsdk.domain.models.BaseModel

internal data class BlockDataItem(

	@SerializedName("canned_response_id")
	var cannedResponseId: Any? = null,

	@SerializedName("ordered_no")
	var orderedNo: Int = 0,

	@SerializedName("block_type")
	var blockType: String = "",

	@SerializedName("fileAttachmentData")
	var fileAttachmentData: FileAttachmentData? = null,

	@SerializedName("is_draft")
	var isDraft: Boolean = false,

	@SerializedName("file_id")
	var fileId: Any? = null,

	@SerializedName("message_id")
	var messageId: String = "",

	@SerializedName("id")
	var id: String = "",

	@SerializedName("text")
	var text: String = "",

	@SerializedName("body")
	var body: Any? = null,

	@SerializedName("linkName")
	var linkName: String = ""
) : BaseModel()
