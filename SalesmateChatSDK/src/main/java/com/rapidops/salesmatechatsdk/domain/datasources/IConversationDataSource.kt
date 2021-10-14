package com.rapidops.salesmatechatsdk.domain.datasources

import com.rapidops.salesmatechatsdk.data.resmodels.ConversationDetailRes
import com.rapidops.salesmatechatsdk.data.resmodels.ConversationRes

internal interface IConversationDataSource {

    suspend fun getConversationList(rows: Int, offSet: Int): ConversationRes
    suspend fun getConversationsDetail(conversationId: String): ConversationDetailRes

}