package com.rapidops.salesmatechatsdk.domain.usecases

import com.rapidops.salesmatechatsdk.data.reqmodels.SendMessageReq
import com.rapidops.salesmatechatsdk.data.resmodels.SendMessageRes
import com.rapidops.salesmatechatsdk.domain.datasources.IAppSettingsDataSource
import com.rapidops.salesmatechatsdk.domain.datasources.IConversationDataSource
import com.rapidops.salesmatechatsdk.domain.models.message.MessageType
import java.util.*
import javax.inject.Inject


internal class SendMessageUseCase @Inject constructor(
    private val appSettingsDataSource: IAppSettingsDataSource,
    private val conversationDataSource: IConversationDataSource,
) :
    UseCase<SendMessageUseCase.Param, SendMessageRes>() {


    override suspend fun execute(params: Param?): SendMessageRes {
        val sendMessageParam = params!!

        return conversationDataSource.sendMessages(
            sendMessageParam.conversationId,
            sendMessageParam.sendMessageReq
        )

    }

    fun getNewSendMessageReq(messageId: String? = null): SendMessageReq {
        val uniqueMessageId = messageId ?: UUID.randomUUID().toString()
        val sendMessageReq = SendMessageReq()
        sendMessageReq.apply {
            this.messageType = MessageType.COMMENT.value
            this.messageId = uniqueMessageId
            this.isBot = false
            this.isInbound = true
            this.conversationName = appSettingsDataSource.contactName
        }
        return sendMessageReq
    }

    data class Param(val conversationId: String, val sendMessageReq: SendMessageReq)

}

