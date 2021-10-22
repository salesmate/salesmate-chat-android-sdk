package com.rapidops.salesmatechatsdk.domain.models.message

import com.google.gson.annotations.SerializedName
import com.rapidops.salesmatechatsdk.domain.models.BaseModel

internal data class FileAttachmentData(

	@SerializedName("workspace_id")
	var workspaceId: String = "",

	@SerializedName("gcp_file_name")
	var gcpFileName: String = "",

	@SerializedName("thumbnail")
	var thumbnail: String? = null,

	@SerializedName("content_type")
	var contentType: String = "",

	@SerializedName("size")
	var size: String = "",

	@SerializedName("conversation_id")
	var conversationId: String = "",

	@SerializedName("name")
	var name: String = "",

	@SerializedName("link_url")
	var linkUrl: Any? = null,

	@SerializedName("id")
	var id: String = "",

	@SerializedName("linkname")
	var linkname: String = "",

	@SerializedName("url")
	var url: String = ""
) : BaseModel() {
	var gcpThumbnailFileName: String? = null
}
