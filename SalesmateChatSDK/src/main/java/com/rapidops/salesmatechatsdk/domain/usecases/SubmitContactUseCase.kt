package com.rapidops.salesmatechatsdk.domain.usecases

import com.rapidops.salesmatechatsdk.domain.datasources.IAppSettingsDataSource
import com.rapidops.salesmatechatsdk.domain.datasources.IConversationDataSource
import javax.inject.Inject


internal class SubmitContactUseCase @Inject constructor(
    private val appSettingsDataSource: IAppSettingsDataSource,
    private val conversationDataSource: IConversationDataSource,
) :
    UseCase<SubmitContactUseCase.Param, Boolean>() {


    override suspend fun execute(params: Param?): Boolean {
        val submitContactParam = params!!

        conversationDataSource.contact(
            submitContactParam.conversationId,
            submitContactParam.email
        )
        return true

    }

    data class Param(val conversationId: String?, val email: String)

}

