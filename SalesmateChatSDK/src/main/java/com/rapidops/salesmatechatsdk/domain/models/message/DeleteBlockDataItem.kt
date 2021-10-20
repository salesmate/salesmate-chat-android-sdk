package com.rapidops.salesmatechatsdk.domain.models.message

import com.google.gson.annotations.SerializedName

internal data class DeleteBlockDataItem(

    @SerializedName("text")
    var text: String = ""

) : BlockDataItem()
