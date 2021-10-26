package com.rapidops.salesmatechatsdk.domain.usecases

import com.rapidops.salesmatechatsdk.data.resmodels.ConversationDetailRes
import com.rapidops.salesmatechatsdk.domain.datasources.IAppSettingsDataSource
import com.rapidops.salesmatechatsdk.domain.datasources.IConversationDataSource
import javax.inject.Inject


internal class ReadConversationForVisitorUseCase @Inject constructor(
    private val appSettingsDataSource: IAppSettingsDataSource,
    private val conversationDataSource: IConversationDataSource,
) :
    UseCase<ReadConversationForVisitorUseCase.Param, ConversationDetailRes>() {


    override suspend fun execute(params: Param?): ConversationDetailRes {
        val readConversationForVisitorParam = params!!

        return conversationDataSource.readConversationForVisitor(readConversationForVisitorParam.conversationId)

    }

    data class Param(val conversationId: String)

}

