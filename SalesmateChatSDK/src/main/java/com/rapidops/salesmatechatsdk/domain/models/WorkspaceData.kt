package com.rapidops.salesmatechatsdk.domain.models


import com.google.gson.annotations.SerializedName

internal data class WorkspaceData(
    @SerializedName("description")
    var description: String = "",
    @SerializedName("id")
    var id: String = "",
    @SerializedName("linkname")
    var linkname: String = "",
    @SerializedName("name")
    var name: String = ""
) : BaseModel()