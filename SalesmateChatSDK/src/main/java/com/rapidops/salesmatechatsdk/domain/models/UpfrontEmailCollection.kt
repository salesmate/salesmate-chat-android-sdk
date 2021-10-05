package com.rapidops.salesmatechatsdk.domain.models


import com.google.gson.annotations.SerializedName

internal data class UpfrontEmailCollection(
    @SerializedName("frequency")
    var frequency: String = ""
) : BaseModel()