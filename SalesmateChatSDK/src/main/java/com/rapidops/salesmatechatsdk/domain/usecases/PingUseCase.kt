package com.rapidops.salesmatechatsdk.domain.usecases

import com.rapidops.salesmatechatsdk.domain.datasources.IAppSettingsDataSource
import com.rapidops.salesmatechatsdk.domain.datasources.IAuthDataSource
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.coroutineScope
import javax.inject.Inject

internal class PingUseCase @Inject constructor(
    private val appSettingsDataSource: IAppSettingsDataSource,
    private val authDataSource: IAuthDataSource
) :
    UseCase<Nothing, Unit>() {

    override suspend fun execute(params: Nothing?) {
        val pingRes =
            authDataSource.ping(
                appSettingsDataSource.salesMateChatSetting.tenantId,
                appSettingsDataSource.pseudoName
            )
        if (pingRes.pseudoName.isNotEmpty()) {
            appSettingsDataSource.pseudoName = pingRes.pseudoName
        }
        appSettingsDataSource.contactData = pingRes.contactData
        appSettingsDataSource.pingRes = pingRes
    }


}

