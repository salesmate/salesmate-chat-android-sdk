package com.rapidops.salesmatechatsdk.domain.usecases

import com.rapidops.salesmatechatsdk.domain.datasources.IAppSettingsDataSource
import com.rapidops.salesmatechatsdk.domain.datasources.IConversationDataSource
import com.rapidops.salesmatechatsdk.domain.models.ConversationDetailItem
import com.rapidops.salesmatechatsdk.domain.models.User
import javax.inject.Inject


internal class GetConversationDetailUseCase @Inject constructor(
    private val appSettingsDataSource: IAppSettingsDataSource,
    private val conversationDataSource: IConversationDataSource
) :
    UseCase<GetConversationDetailUseCase.Param, ConversationDetailItem>() {


    override suspend fun execute(params: Param?): ConversationDetailItem {
        val conversationParam = params!!
        val conversationsDetailRes =
            conversationDataSource.getConversationsDetail(conversationParam.conversationId)
        val conversationDetailItem = ConversationDetailItem()
        val workspaceData = appSettingsDataSource.pingRes.workspaceData
        val user = conversationsDetailRes.conversations?.let { conversations ->
            if (conversations.lastParticipatingUserId.isEmpty()) {
                User(id = null, firstName = workspaceData?.name ?: "")
            } else {
                appSettingsDataSource.pingRes.users.find { it.id == conversations.lastParticipatingUserId }
            }
        } ?: run {
            User(id = null, firstName = workspaceData?.name ?: "")
        }
        conversationDetailItem.conversations = conversationsDetailRes.conversations
        conversationDetailItem.user = user


        return conversationDetailItem
    }

    data class Param(val conversationId: String)

}

