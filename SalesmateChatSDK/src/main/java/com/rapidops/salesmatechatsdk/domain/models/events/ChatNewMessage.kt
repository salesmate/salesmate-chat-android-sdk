package com.rapidops.salesmatechatsdk.domain.models.events

import com.google.gson.annotations.SerializedName
import com.rapidops.salesmatechatsdk.domain.models.BaseModel

internal data class ChatNewMessage(
    @SerializedName("conversationId")
    var conversationId: String = "",
    @SerializedName("createdDate")
    var createdDate: String = "",
    @SerializedName("messageId")
    var messageId: String = "",
) : BaseModel()
