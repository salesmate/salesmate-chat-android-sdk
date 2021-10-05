package com.rapidops.salesmatechatsdk.domain.models


import com.google.gson.annotations.SerializedName

internal data class Location(
    @SerializedName("city")
    var city: String = "",
    @SerializedName("country")
    var country: String = "",
    @SerializedName("timezone")
    var timezone: String = ""
) : BaseModel()