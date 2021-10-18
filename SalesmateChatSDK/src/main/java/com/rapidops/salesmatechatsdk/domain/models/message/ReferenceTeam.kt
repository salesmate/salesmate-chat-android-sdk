package com.rapidops.salesmatechatsdk.domain.models.message

import com.google.gson.annotations.SerializedName
import com.rapidops.salesmatechatsdk.domain.models.BaseModel

internal data class ReferenceTeam(

    @SerializedName("id")
    var id: String = "",

    @SerializedName("name")
    var name: String = "",

) : BaseModel()