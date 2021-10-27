package com.rapidops.salesmatechatsdk.domain.usecases

import com.rapidops.salesmatechatsdk.domain.datasources.IAppSettingsDataSource
import com.rapidops.salesmatechatsdk.domain.datasources.IConversationDataSource
import javax.inject.Inject


internal class SubmitRemarkUseCase @Inject constructor(
    private val appSettingsDataSource: IAppSettingsDataSource,
    private val conversationDataSource: IConversationDataSource,
) :
    UseCase<SubmitRemarkUseCase.Param, Boolean>() {


    override suspend fun execute(params: Param?): Boolean {
        val submitRatingParam = params!!
        conversationDataSource.remark(
            submitRatingParam.conversationId,
            submitRatingParam.remark
        )
        return true
    }

    data class Param(val conversationId: String, val remark: String)

}

