package com.rapidops.salesmatechatsdk.data.repositories

import com.rapidops.salesmatechatsdk.data.resmodels.ConversationRes
import com.rapidops.salesmatechatsdk.data.webserivce.IService
import com.rapidops.salesmatechatsdk.domain.datasources.IConversationDataSource
import com.rapidops.salesmatechatsdk.domain.exception.APIResponseMapper

internal class ConversationRepository(private val service: IService) : IConversationDataSource {

    override suspend fun getConversations(rows: Int, offSet: Int): ConversationRes {
        return APIResponseMapper.getResponse {
            service.getConversations(rows, offSet)
        }
    }
}