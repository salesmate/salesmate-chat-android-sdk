package com.rapidops.salesmatechatsdk.domain.usecases

import com.rapidops.salesmatechatsdk.domain.datasources.IAppSettingsDataSource
import com.rapidops.salesmatechatsdk.domain.datasources.IConversationDataSource
import com.rapidops.salesmatechatsdk.domain.models.ConversationDetailItem
import javax.inject.Inject


internal class GetConversationUseCase @Inject constructor(
    private val appSettingsDataSource: IAppSettingsDataSource,
    private val conversationDataSource: IConversationDataSource
) :
    UseCase<GetConversationUseCase.Param, List<ConversationDetailItem>>() {


    override suspend fun execute(params: Param?): List<ConversationDetailItem> {
        val conversationParam = params!!
        val conversationsRes = conversationDataSource.getConversations(
            conversationParam.rows,
            conversationParam.offSet
        )
        val conversationDetailItemList = arrayListOf<ConversationDetailItem>()
        conversationsRes.conversationList.forEach { conversations ->
            val user =
                appSettingsDataSource.pingRes.users.find { it.id == conversations.lastParticipatingUserId }
            conversationDetailItemList.add(ConversationDetailItem(conversations, user))
        }
        return conversationDetailItemList
    }

    data class Param(val rows: Int, val offSet: Int)

}

