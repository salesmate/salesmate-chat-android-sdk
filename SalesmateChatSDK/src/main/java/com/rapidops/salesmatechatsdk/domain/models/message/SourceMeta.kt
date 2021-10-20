package com.rapidops.salesmatechatsdk.domain.models.message

import com.google.gson.annotations.SerializedName
import com.rapidops.salesmatechatsdk.domain.models.BaseModel

internal data class SourceMeta(

    @SerializedName("channel_type")
    var channelType: String = "",

    @SerializedName("url")
    var url: String = "",

    @SerializedName("user_agent")
    var userAgent: String = ""
) : BaseModel()