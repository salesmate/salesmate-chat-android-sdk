package com.rapidops.salesmatechatsdk.domain.models

internal data class ConversationDetailItem(
    var conversations: Conversations? = null,
    var user: User? = null,
) : BaseModel()