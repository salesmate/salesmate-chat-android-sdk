package com.rapidops.salesmatechatsdk.domain.models.message

import com.google.gson.annotations.SerializedName
import com.rapidops.salesmatechatsdk.domain.models.BaseModel
import com.rapidops.salesmatechatsdk.domain.models.BlockType

internal open class FileBlockDataItem(

	@SerializedName("fileAttachmentData")
	var fileAttachmentData: FileAttachmentData? = null,

) : BlockDataItem()
