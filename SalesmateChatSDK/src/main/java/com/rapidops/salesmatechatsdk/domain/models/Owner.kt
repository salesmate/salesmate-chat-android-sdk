package com.rapidops.salesmatechatsdk.domain.models

import com.google.gson.annotations.SerializedName

internal data class Owner(
    @SerializedName("id")
    var id: String = "",
    @SerializedName("name")
    var name: String = "",
    @SerializedName("firstName")
    var firstName: String = "",
    @SerializedName("lastName")
    var lastName: String = "",
    @SerializedName("photo")
    var photo: String = "",
    @SerializedName("email")
    var email: String = "",
    @SerializedName("mobile")
    var mobile: String = "",
) : BaseModel()
