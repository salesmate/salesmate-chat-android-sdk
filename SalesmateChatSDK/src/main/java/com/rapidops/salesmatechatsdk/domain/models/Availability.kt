package com.rapidops.salesmatechatsdk.domain.models


import com.google.gson.annotations.SerializedName

internal data class Availability(
    @SerializedName("calculate_response_time_in_office_hours")
    var calculateResponseTimeInOfficeHours: Any? = null,
    @SerializedName("office_hours")
    var officeHours: List<OfficeHour> = listOf(),
    @SerializedName("reply_time")
    var replyTime: String = "",
    @SerializedName("timezone")
    var timezone: String = ""
) : BaseModel()