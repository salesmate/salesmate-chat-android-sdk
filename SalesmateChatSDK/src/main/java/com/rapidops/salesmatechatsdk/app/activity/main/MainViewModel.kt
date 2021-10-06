package com.rapidops.salesmatechatsdk.app.activity.main

import com.rapidops.salesmatechatsdk.app.base.BaseViewModel
import com.rapidops.salesmatechatsdk.app.coroutines.ICoroutineContextProvider
import com.rapidops.salesmatechatsdk.app.utils.SingleLiveEvent
import com.rapidops.salesmatechatsdk.domain.datasources.IAppSettingsDataSource
import com.rapidops.salesmatechatsdk.domain.usecases.PingAndGenerateTokenUseCase
import kotlinx.coroutines.withContext
import javax.inject.Inject

internal class MainViewModel @Inject constructor(
    private val coroutineContextProvider: ICoroutineContextProvider,
    private val pingAndGenerateTokenUseCase: PingAndGenerateTokenUseCase,
    private val appSettingsDataSource: IAppSettingsDataSource,
) : BaseViewModel(coroutineContextProvider) {

    val showRecentChatList = SingleLiveEvent<Nothing>()

    fun subscribe() {
        if (appSettingsDataSource.pingRes.linkname.isEmpty()) {
            withProgress({
                pingAndGenerateTokenUseCase.execute()
                withContext(coroutineContextProvider.ui) {
                    showRecentChatList.call()
                }
            }, {
                defaultErrorHandler(it)
            })
        } else {
            showRecentChatList.call()
        }

    }

}