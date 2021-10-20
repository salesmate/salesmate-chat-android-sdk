package com.rapidops.salesmatechatsdk.domain.usecases

import com.rapidops.salesmatechatsdk.domain.datasources.IAppSettingsDataSource
import com.rapidops.salesmatechatsdk.domain.datasources.IConversationDataSource
import com.rapidops.salesmatechatsdk.domain.models.ConversationDetailItem
import javax.inject.Inject


internal class GetConversationDetailUseCase @Inject constructor(
    private val appSettingsDataSource: IAppSettingsDataSource,
    private val conversationDataSource: IConversationDataSource,
    private val getUserFromUserIdUseCase: GetUserFromUserIdUseCase
) :
    UseCase<GetConversationDetailUseCase.Param, ConversationDetailItem>() {


    override suspend fun execute(params: Param?): ConversationDetailItem {
        val conversationParam = params!!
        val conversationsDetailRes =
            conversationDataSource.getConversationsDetail(conversationParam.conversationId)
        val conversationDetailItem = ConversationDetailItem()

        val user =
            getUserFromUserIdUseCase.execute(conversationsDetailRes.conversations?.lastParticipatingUserId)

        conversationDetailItem.conversations = conversationsDetailRes.conversations
        conversationDetailItem.user = user


        return conversationDetailItem
    }

    data class Param(val conversationId: String)

}

