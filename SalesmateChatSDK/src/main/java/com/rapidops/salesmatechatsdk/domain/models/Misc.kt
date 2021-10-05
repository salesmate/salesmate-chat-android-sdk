package com.rapidops.salesmatechatsdk.domain.models


import com.google.gson.annotations.SerializedName

internal data class Misc(
    @SerializedName("gif_support_enabled")
    var gifSupportEnabled: Boolean = false,
    @SerializedName("play_sounds_for_messenger")
    var playSoundsForMessenger: Boolean = false
) : BaseModel()