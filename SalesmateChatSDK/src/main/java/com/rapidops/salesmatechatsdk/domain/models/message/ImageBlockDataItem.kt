package com.rapidops.salesmatechatsdk.domain.models.message

import com.google.gson.annotations.SerializedName

internal class ImageBlockDataItem(

    @SerializedName("fileAttachmentData")
    var fileAttachmentData: FileAttachmentData? = null,

    ) : BlockDataItem()