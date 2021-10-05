package com.rapidops.salesmatechatsdk.domain.models


import com.google.gson.annotations.SerializedName

internal data class OfficeHour(
    @SerializedName("endTime")
    var endTime: String = "",
    @SerializedName("startTime")
    var startTime: String = "",
    @SerializedName("weekName")
    var weekName: String = ""
) : BaseModel()