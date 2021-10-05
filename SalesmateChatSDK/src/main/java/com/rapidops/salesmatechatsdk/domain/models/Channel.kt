package com.rapidops.salesmatechatsdk.domain.models


import com.google.gson.annotations.SerializedName

internal data class Channel(

    @SerializedName("channels")
    var channels: ChannelsItem? = null,

    @SerializedName("events")
    var events: Events? = null,
) : BaseModel()