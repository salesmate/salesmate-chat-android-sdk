package com.rapidops.salesmatechatsdk.domain.models.events

import com.google.gson.annotations.SerializedName
import com.rapidops.salesmatechatsdk.domain.models.BaseModel

internal data class TypingMessage(
    @SerializedName("conversationId")
    var conversationId: String = "",
    @SerializedName("userId")
    var userId: String = "",
    @SerializedName("workspaceId")
    var workspaceId: String = "",
    @SerializedName("messageType")
    var messageType: String = ""
) : BaseModel()
