package com.rapidops.salesmatechatsdk.domain.usecases

import com.rapidops.salesmatechatsdk.domain.datasources.IAppSettingsDataSource
import com.rapidops.salesmatechatsdk.domain.datasources.IConversationDataSource
import com.rapidops.salesmatechatsdk.domain.models.ConversationDetailItem
import com.rapidops.salesmatechatsdk.domain.models.User
import javax.inject.Inject


internal class GetConversationListUseCase @Inject constructor(
    private val appSettingsDataSource: IAppSettingsDataSource,
    private val conversationDataSource: IConversationDataSource
) :
    UseCase<GetConversationListUseCase.Param, List<ConversationDetailItem>>() {


    override suspend fun execute(params: Param?): List<ConversationDetailItem> {
        val conversationParam = params!!
        val conversationsRes = conversationDataSource.getConversationList(
            conversationParam.rows,
            conversationParam.offSet
        )
        val conversationDetailItemList = arrayListOf<ConversationDetailItem>()
        val workspaceData = appSettingsDataSource.pingRes.workspaceData
        conversationsRes.conversationList.forEach { conversations ->
            val user = if (conversations.lastParticipatingUserId.isEmpty()) {
                User(id = null, firstName = workspaceData?.name ?: "")
            } else {
                appSettingsDataSource.pingRes.users.find { it.id == conversations.lastParticipatingUserId }
            }

            conversationDetailItemList.add(ConversationDetailItem(conversations, user))
        }
        return conversationDetailItemList
    }

    data class Param(val rows: Int, val offSet: Int)

}

