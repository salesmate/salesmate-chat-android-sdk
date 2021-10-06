package com.rapidops.salesmatechatsdk.data.resmodels


import com.rapidops.salesmatechatsdk.domain.models.Conversations

internal data class ConversationRes(

    var conversationList: List<Conversations> = listOf(),

    ) : BaseRes()