package com.rapidops.salesmatechatsdk.app.activity.main

import com.rapidops.salesmatechatsdk.app.base.BaseViewModel
import com.rapidops.salesmatechatsdk.app.coroutines.ICoroutineContextProvider
import com.rapidops.salesmatechatsdk.app.socket.SocketController
import com.rapidops.salesmatechatsdk.app.utils.AppEvent
import com.rapidops.salesmatechatsdk.app.utils.EventBus
import com.rapidops.salesmatechatsdk.app.utils.SingleLiveEvent
import com.rapidops.salesmatechatsdk.domain.datasources.IAppSettingsDataSource
import com.rapidops.salesmatechatsdk.domain.usecases.GetConversationDetailUseCase
import com.rapidops.salesmatechatsdk.domain.usecases.PingAndGenerateTokenUseCase
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.filterIsInstance
import kotlinx.coroutines.withContext
import javax.inject.Inject

internal class MainViewModel @Inject constructor(
    private val coroutineContextProvider: ICoroutineContextProvider,
    private val pingAndGenerateTokenUseCase: PingAndGenerateTokenUseCase,
    private val appSettingsDataSource: IAppSettingsDataSource,
    private val socketController: SocketController,
    private val getConversationDetailUseCase: GetConversationDetailUseCase,
) : BaseViewModel(coroutineContextProvider) {

    val showRecentChatList = SingleLiveEvent<Nothing>()

    fun subscribe() {
        if (appSettingsDataSource.linkName.isEmpty()) {
            withProgress({
                pingAndGenerateTokenUseCase.execute()
                socketController.connect()
                withContext(coroutineContextProvider.ui) {
                    showRecentChatList.call()
                }
            }, {
                defaultErrorHandler(it)
            })
        } else {
            showRecentChatList.call()
        }


        subscribeEvent {
            EventBus.events.filterIsInstance<AppEvent.NewMessageEvent>().collectLatest { event ->
                loadConversationDetail(event.data.conversationId)
            }
        }
    }

    private fun loadConversationDetail(conversationId: String) {
        withoutProgress({
            val conversationDetailItem = getConversationDetailUseCase.execute(
                GetConversationDetailUseCase.Param(conversationId)
            )
            withContext(coroutineContextProvider.ui) {
                EventBus.fireEvent(AppEvent.UpdateConversationDetailEvent(conversationDetailItem))
            }
        }, {})
    }
}