package com.rapidops.salesmatechatsdk.domain.models


import com.google.gson.annotations.SerializedName

internal data class EmojiMapping(
    @SerializedName("id")
    var id: String = "",
    @SerializedName("label")
    var label: String = "",
    @SerializedName("unicode")
    var unicode: String = ""
) : BaseModel()