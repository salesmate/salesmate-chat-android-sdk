package com.rapidops.salesmatechatsdk.domain.usecases

import com.rapidops.salesmatechatsdk.data.reqmodels.SendMessageReq
import com.rapidops.salesmatechatsdk.data.resmodels.SendMessageRes
import com.rapidops.salesmatechatsdk.domain.datasources.IAppSettingsDataSource
import com.rapidops.salesmatechatsdk.domain.datasources.IConversationDataSource
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

    data class Param(val conversationId: String, val sendMessageReq: SendMessageReq)

}

