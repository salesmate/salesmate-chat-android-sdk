package com.rapidops.salesmatechatsdk.domain.models


import com.google.gson.annotations.SerializedName

internal data class User(
    @SerializedName("availability_mode")
    var availabilityMode: String = "",
    @SerializedName("availability_status")
    var availabilityStatus: String = "",
    @SerializedName("firstName")
    var firstName: String = "",
    @SerializedName("id")
    var id: Any? = null,
    @SerializedName("lastName")
    var lastName: String = "",
    @SerializedName("last_seen_at")
    var lastSeenAt: String = "",
    @SerializedName("linkname")
    var linkname: String = "",
    @SerializedName("location")
    var location: Location? = null,
    @SerializedName("profileId")
    var profileId: Int = 0,
    @SerializedName("profileUrl")
    var profileUrl: String = "",
    @SerializedName("roleId")
    var roleId: Int = 0,
    @SerializedName("status")
    var status: String = ""
) : BaseModel()