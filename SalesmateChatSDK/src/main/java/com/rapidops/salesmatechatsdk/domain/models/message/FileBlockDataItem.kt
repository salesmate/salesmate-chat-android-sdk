package com.rapidops.salesmatechatsdk.domain.models.message

import com.google.gson.annotations.SerializedName

internal open class FileBlockDataItem(

	@SerializedName("fileAttachmentData")
	var fileAttachmentData: FileAttachmentData? = null,

) : BlockDataItem()
