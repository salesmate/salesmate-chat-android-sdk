package com.rapidops.salesmatechatsdk.data.resmodels

import com.rapidops.salesmatechatsdk.domain.models.message.MessageItem

internal data class MessageListRes(

    var messageList: List<MessageItem> = listOf(),

    ) : BaseRes()