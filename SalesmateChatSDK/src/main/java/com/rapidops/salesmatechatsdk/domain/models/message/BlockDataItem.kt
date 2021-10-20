package com.rapidops.salesmatechatsdk.domain.models.message

import com.google.gson.annotations.SerializedName
import com.rapidops.salesmatechatsdk.domain.models.BaseModel
import com.rapidops.salesmatechatsdk.domain.models.BlockType

internal open class BlockDataItem(

	@SerializedName("canned_response_id")
	var cannedResponseId: Any? = null,

	@SerializedName("ordered_no")
	var orderedNo: Int = 0,

	@SerializedName("block_type")
	var blockType: BlockType = BlockType.TEXT,

	@SerializedName("is_draft")
	var isDraft: Boolean = false,

	@SerializedName("file_id")
	var fileId: Any? = null,

	@SerializedName("message_id")
	var messageId: String = "",

	@SerializedName("id")
	var id: String = "",

	@SerializedName("body")
	var body: Any? = null,

	@SerializedName("linkName")
	var linkName: String = ""
) : BaseModel() {
	var isSelfMessage: Boolean = false
}
