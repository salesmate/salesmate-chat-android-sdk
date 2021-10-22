package com.rapidops.salesmatechatsdk.data.resmodels


import com.google.gson.annotations.SerializedName

internal data class UploadFileRes(

    @SerializedName("path")
    var path: String = "",

    @SerializedName("fileName")
    var fileName: String = "",

    @SerializedName("contentType")
    var contentType: String = "",

    @SerializedName("url")
    var url: String = "",

    @SerializedName("thumbnailPath")
    var thumbnailPath: String? = null,

    @SerializedName("thumbnailUrl")
    var thumbnailUrl: String? = null

) : BaseRes()