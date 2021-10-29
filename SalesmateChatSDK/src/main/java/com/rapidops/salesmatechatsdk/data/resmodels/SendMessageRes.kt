package com.rapidops.salesmatechatsdk.data.resmodels


import com.google.gson.annotations.SerializedName

internal data class SendMessageRes(

    @SerializedName("status")
    var status: String = "",

    @SerializedName("isEmailAsked")
    var isEmailAsked: Boolean = false

) : BaseRes() {

    val isSuccess: Boolean
        get() = status.equals("success", true)
}