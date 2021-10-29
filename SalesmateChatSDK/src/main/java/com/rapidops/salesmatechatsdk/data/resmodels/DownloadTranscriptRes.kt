package com.rapidops.salesmatechatsdk.data.resmodels


import com.google.gson.annotations.SerializedName

internal data class DownloadTranscriptRes(

    @SerializedName("Status")
    var status: String = "",

    @SerializedName("Data")
    var data: String = "",

    ) : BaseRes() {

    val isSuccess: Boolean
        get() = status.equals("success", true)
}