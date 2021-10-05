package com.rapidops.salesmatechatsdk.domain.models


import com.google.gson.annotations.SerializedName

internal data class Events(
    @SerializedName("visitorIsTyping")
    var visitorIsTyping: String = "",
    @SerializedName("widgetUserPreference")
    var widgetUserPreference: String = "",
) : BaseModel()