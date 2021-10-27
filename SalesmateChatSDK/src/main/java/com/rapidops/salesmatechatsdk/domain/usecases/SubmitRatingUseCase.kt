package com.rapidops.salesmatechatsdk.domain.usecases

import com.rapidops.salesmatechatsdk.domain.datasources.IAppSettingsDataSource
import com.rapidops.salesmatechatsdk.domain.datasources.IConversationDataSource
import javax.inject.Inject


internal class SubmitRatingUseCase @Inject constructor(
    private val appSettingsDataSource: IAppSettingsDataSource,
    private val conversationDataSource: IConversationDataSource,
) :
    UseCase<SubmitRatingUseCase.Param, Boolean>() {


    override suspend fun execute(params: Param?): Boolean {
        val submitRatingParam = params!!

        conversationDataSource.rating(
            submitRatingParam.conversationId,
            submitRatingParam.rating
        )
        return true

    }

    data class Param(val conversationId: String, val rating: String)

}

