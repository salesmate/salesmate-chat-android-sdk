package com.rapidops.salesmatechatsdk.app.interfaces

import com.rapidops.salesmatechatsdk.domain.models.ConversationDetailItem
import com.rapidops.salesmatechatsdk.domain.models.message.MessageItem

internal interface MessageAdapterListener {
    fun onInfoClick(messageItem: MessageItem)
    fun getConversationDetail(): ConversationDetailItem?
    fun submitRemark(remark: String)
    fun submitRating(rating: String)
    fun submitContact(name: String, email: String)

}