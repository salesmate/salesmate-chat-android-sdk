package com.rapidops.salesmatechatsdk.app.interfaces

import com.rapidops.salesmatechatsdk.domain.models.message.MessageItem

internal interface MessageAdapterListener {
    fun onInfoClick(messageItem: MessageItem)

}