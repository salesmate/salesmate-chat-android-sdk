package com.rapidops.salesmatechatsdk.domain.usecases

import com.rapidops.salesmatechatsdk.domain.datasources.IAnalyticsDataSource
import com.rapidops.salesmatechatsdk.domain.datasources.IAppSettingsDataSource
import javax.inject.Inject


internal class SendAnalyticsUseCase @Inject constructor(
    private val appSettingsDataSource: IAppSettingsDataSource,
    private val analyticsDataSource: IAnalyticsDataSource,
) :
    UseCase<SendAnalyticsUseCase.Param, Boolean>() {


    override suspend fun execute(params: Param?): Boolean {
        val trackEventParams = params!!

        analyticsDataSource.sendEvent(trackEventParams.eventName, trackEventParams.data)

        return true
    }

    data class Param(val eventName: String, val data: HashMap<String, String>)

}

