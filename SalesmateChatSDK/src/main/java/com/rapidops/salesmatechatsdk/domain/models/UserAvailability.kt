package com.rapidops.salesmatechatsdk.domain.models

import com.google.gson.annotations.SerializedName

internal data class UserAvailability(
    @SerializedName("userIds")
    var userIds: ArrayList<String> = arrayListOf(),
    @SerializedName("status")
    var status: String = "",
) : BaseModel()
