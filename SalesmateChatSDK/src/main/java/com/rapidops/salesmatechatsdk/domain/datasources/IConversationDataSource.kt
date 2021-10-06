package com.rapidops.salesmatechatsdk.domain.datasources

import com.rapidops.salesmatechatsdk.data.resmodels.ConversationRes

internal interface IConversationDataSource {

    suspend fun getConversations(rows: Int, offSet: Int): ConversationRes
}