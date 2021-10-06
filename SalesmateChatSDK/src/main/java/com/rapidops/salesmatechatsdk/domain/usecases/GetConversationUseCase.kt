package com.rapidops.salesmatechatsdk.domain.usecases

import com.rapidops.salesmatechatsdk.data.resmodels.ConversationRes
import com.rapidops.salesmatechatsdk.domain.datasources.IConversationDataSource
import javax.inject.Inject


internal class GetConversationUseCase @Inject constructor(private val conversationDataSource: IConversationDataSource) :
    UseCase<GetConversationUseCase.Param, ConversationRes>() {


    override suspend fun execute(params: Param?): ConversationRes {
        val conversationParam = params!!
        return conversationDataSource.getConversations(
            conversationParam.rows,
            conversationParam.offSet
        )
    }

    data class Param(val rows: Int, val offSet: Int)

}

