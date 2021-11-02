package com.rapidops.salesmatechatsdk.domain.usecases

import com.rapidops.salesmatechatsdk.domain.datasources.IAppSettingsDataSource
import com.rapidops.salesmatechatsdk.domain.datasources.IConversationDataSource
import com.rapidops.salesmatechatsdk.domain.models.ConversationDetailItem
import javax.inject.Inject


internal class GetConversationListUseCase @Inject constructor(
    private val appSettingsDataSource: IAppSettingsDataSource,
    private val conversationDataSource: IConversationDataSource,
    private val getUserFromUserIdUseCase: GetUserFromUserIdUseCase

) :
    UseCase<GetConversationListUseCase.Param, List<ConversationDetailItem>>() {


    override suspend fun execute(params: Param?): List<ConversationDetailItem> {
        val conversationParam = params!!
        val conversationsRes = conversationDataSource.getConversationList(
            conversationParam.rows,
            conversationParam.offSet
        )
        val conversationDetailItemList = arrayListOf<ConversationDetailItem>()
        conversationsRes.conversationList.forEach { conversations ->
            val user = getUserFromUserIdUseCase.execute(conversations.lastParticipatingUserId)
            conversationDetailItemList.add(ConversationDetailItem(conversations, user))
        }

        if (appSettingsDataSource.isContact.not()) {
            conversationsRes.conversationList.firstOrNull { it.contactId.isNotEmpty() }?.let {
                appSettingsDataSource.saveContactDetail(it.contactId, it.email, it.name)
            }
        }

        return conversationDetailItemList
    }

    data class Param(val rows: Int, val offSet: Int)

}

