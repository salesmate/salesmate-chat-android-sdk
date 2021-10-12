package com.rapidops.salesmatechatsdk.domain.models

import com.google.gson.annotations.SerializedName

internal data class ContactData(
    @SerializedName("id")
    var id: String = "",
    @SerializedName("name")
    var name: String = "",
    @SerializedName("email")
    var email: String = "",
    @SerializedName("isDeleted")
    var isDeleted: Boolean = false,
    @SerializedName("owner")
    var owner: Owner? = null
) : BaseModel()
