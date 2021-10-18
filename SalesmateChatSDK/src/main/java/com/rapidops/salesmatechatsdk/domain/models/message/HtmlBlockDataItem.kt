package com.rapidops.salesmatechatsdk.domain.models.message

import com.google.gson.annotations.SerializedName

internal data class HtmlBlockDataItem(

    @SerializedName("text")
    var text: String = "",

    ) : BlockDataItem()
