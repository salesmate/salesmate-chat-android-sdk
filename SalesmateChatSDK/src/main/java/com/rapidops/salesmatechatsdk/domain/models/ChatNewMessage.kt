package com.rapidops.salesmatechatsdk.domain.models

import com.google.gson.annotations.SerializedName

internal data class ChatNewMessage(
    @SerializedName("blocks")
    var blocks: ArrayList<String> = arrayListOf(),
    @SerializedName("conversationId")
    var conversationId: String = "",
    @SerializedName("createdDate")
    var createdDate: String = "",
    @SerializedName("messageId")
    var messageId: String = "",
) : BaseModel()
