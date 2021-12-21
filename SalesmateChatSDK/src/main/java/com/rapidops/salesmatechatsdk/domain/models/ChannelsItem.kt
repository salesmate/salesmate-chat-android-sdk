package com.rapidops.salesmatechatsdk.domain.models


import com.google.gson.annotations.SerializedName

internal data class ChannelsItem(
    @SerializedName("tenantSpecificChannelNameForWidget")
    var tenantSpecificChannelNameForWidget: String = "",

    @SerializedName("contactUnVerifiedChannelName")
    var contactUnVerifiedChannelName: String = "",

    @SerializedName("contactVerifiedChannelName")
    var contactVerifiedChannelName: String = "",
) : BaseModel()