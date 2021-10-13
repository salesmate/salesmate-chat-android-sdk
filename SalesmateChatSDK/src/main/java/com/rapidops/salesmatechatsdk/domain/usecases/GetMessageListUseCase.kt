package com.rapidops.salesmatechatsdk.domain.usecases

import com.rapidops.salesmatechatsdk.domain.datasources.IAppSettingsDataSource
import com.rapidops.salesmatechatsdk.domain.datasources.IConversationDataSource
import com.rapidops.salesmatechatsdk.domain.models.message.MessageItem
import javax.inject.Inject


internal class GetMessageListUseCase @Inject constructor(
    private val appSettingsDataSource: IAppSettingsDataSource,
    private val conversationDataSource: IConversationDataSource,
    private val getUserFromUserIdUseCase: GetUserFromUserIdUseCase
) :
    UseCase<GetMessageListUseCase.Param, List<MessageItem>>() {


    override suspend fun execute(params: Param?): List<MessageItem> {
        val messageListParam = params!!
        val messageListRes = conversationDataSource.getMessageList(
            messageListParam.conversationId,
            messageListParam.rows,
            messageListParam.offSet
        )

        messageListRes.messageList.forEach {
            it.user = getUserFromUserIdUseCase.execute(it.userId)
        }

        return messageListRes.messageList.reversed()
    }

    data class Param(val conversationId: String, val rows: Int, val offSet: Int)

}

