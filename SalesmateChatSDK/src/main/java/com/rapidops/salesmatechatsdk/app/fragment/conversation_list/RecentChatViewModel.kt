package com.rapidops.salesmatechatsdk.app.fragment.conversation_list

import com.rapidops.salesmatechatsdk.app.base.BaseViewModel
import com.rapidops.salesmatechatsdk.app.coroutines.ICoroutineContextProvider
import com.rapidops.salesmatechatsdk.app.utils.SingleLiveEvent
import com.rapidops.salesmatechatsdk.data.resmodels.PingRes
import com.rapidops.salesmatechatsdk.domain.datasources.IAppSettingsDataSource
import com.rapidops.salesmatechatsdk.domain.models.Conversations
import com.rapidops.salesmatechatsdk.domain.usecases.GetConversationUseCase
import kotlinx.coroutines.withContext
import javax.inject.Inject

internal class RecentChatViewModel @Inject constructor(
    private val appSettingsDataSource: IAppSettingsDataSource,
    private val coroutineContextProvider: ICoroutineContextProvider,
    private val getConversationUseCase: GetConversationUseCase,
) : BaseViewModel(coroutineContextProvider) {

    val recentViewProgress = SingleLiveEvent<Boolean>()
    val showConversationList = SingleLiveEvent<List<Conversations>>()

    fun subscribe() {
        recentViewProgress.value = true
        withoutProgress({
            val conversationRes = getConversationUseCase.execute(GetConversationUseCase.Param(3, 0))
            withContext(coroutineContextProvider.ui) {
                showConversationList.value = conversationRes.conversationList
                recentViewProgress.value = false
            }
        }, {
            defaultErrorHandler(it)
        })
    }


    val pingRes: PingRes by lazy {
        appSettingsDataSource.pingRes
    }

}