package com.rapidops.salesmatechatsdk.domain.datasources

import com.rapidops.salesmatechatsdk.data.reqmodels.SendMessageReq
import com.rapidops.salesmatechatsdk.data.resmodels.ConversationDetailRes
import com.rapidops.salesmatechatsdk.data.resmodels.ConversationRes
import com.rapidops.salesmatechatsdk.data.resmodels.MessageListRes
import com.rapidops.salesmatechatsdk.data.resmodels.SendMessageRes

internal interface IConversationDataSource {

    suspend fun getConversationList(rows: Int, offSet: Int): ConversationRes
    suspend fun getConversationsDetail(conversationId: String): ConversationDetailRes
    suspend fun getMessageList(
        conversationId: String,
        rows: Int,
        offSet: Int,
        lastMessageDate: String?
    ): MessageListRes

    suspend fun sendMessages(conversationId: String, sendMessageReq: SendMessageReq): SendMessageRes
}