package com.rapidops.salesmatechatsdk.data.resmodels


import com.rapidops.salesmatechatsdk.domain.models.Conversations

internal data class ConversationDetailRes(

    var conversations: Conversations? = null,

    ) : BaseRes()