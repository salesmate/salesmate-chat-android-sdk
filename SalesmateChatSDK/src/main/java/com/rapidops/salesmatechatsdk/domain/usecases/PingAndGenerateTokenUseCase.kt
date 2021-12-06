package com.rapidops.salesmatechatsdk.domain.usecases

import com.rapidops.salesmatechatsdk.domain.datasources.IAppSettingsDataSource
import com.rapidops.salesmatechatsdk.domain.datasources.IAuthDataSource
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.coroutineScope
import javax.inject.Inject


internal class PingAndGenerateTokenUseCase @Inject constructor(
    private val appSettingsDataSource: IAppSettingsDataSource,
    private val authDataSource: IAuthDataSource
) :
    UseCase<Nothing, Unit>() {

    override suspend fun execute(params: Nothing?) {
        coroutineScope {
            awaitAll(
                async {
                    val pingRes =
                        authDataSource.ping(
                            appSettingsDataSource.salesMateChatSetting.tenantId,
                            appSettingsDataSource.pseudoName
                        )
                    if (pingRes.pseudoName.isNotEmpty()) {
                        appSettingsDataSource.pseudoName = pingRes.pseudoName
                    }
                    appSettingsDataSource.pingRes = pingRes
                },
                async {
                    val generateToken = authDataSource.generateToken(
                        appSettingsDataSource.accessToken,
                        appSettingsDataSource.pseudoName
                    )
                    generateToken.channel?.let {
                        appSettingsDataSource.channel = it
                    }
                    appSettingsDataSource.accessToken = generateToken.authToken
                    if(generateToken.pseudoName.isNotEmpty()) {
                        appSettingsDataSource.pseudoName = generateToken.pseudoName
                    }
                }
            )
        }

        /*val ping = authDataSource.ping(appSettingsDataSource.salesMateChatSetting.tenantId)
        val generateToken = authDataSource.generateToken(
            appSettingsDataSource.accessToken,
            appSettingsDataSource.pseudoName
        )*/
    }


}

