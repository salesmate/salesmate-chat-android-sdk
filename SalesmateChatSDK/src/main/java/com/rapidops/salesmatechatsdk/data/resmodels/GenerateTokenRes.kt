package com.rapidops.salesmatechatsdk.data.resmodels


import com.google.gson.annotations.SerializedName
import com.rapidops.salesmatechatsdk.domain.models.Channel

internal data class GenerateTokenRes(

    @SerializedName("pseudoName")
    var pseudoName: String = "",

    @SerializedName("authToken")
    var authToken: String = "",

    @SerializedName("channel")
    var channel: Channel? = null,
) : BaseRes()