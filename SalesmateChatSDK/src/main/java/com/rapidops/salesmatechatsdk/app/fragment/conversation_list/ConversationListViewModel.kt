package com.rapidops.salesmatechatsdk.app.fragment.conversation_list

import com.rapidops.salesmatechatsdk.app.base.BaseViewModel
import com.rapidops.salesmatechatsdk.app.coroutines.ICoroutineContextProvider
import com.rapidops.salesmatechatsdk.app.utils.AppEvent
import com.rapidops.salesmatechatsdk.app.utils.EventBus
import com.rapidops.salesmatechatsdk.app.utils.SingleLiveEvent
import com.rapidops.salesmatechatsdk.data.resmodels.PingRes
import com.rapidops.salesmatechatsdk.domain.datasources.IAppSettingsDataSource
import com.rapidops.salesmatechatsdk.domain.models.ConversationDetailItem
import com.rapidops.salesmatechatsdk.domain.models.message.MessageItem
import com.rapidops.salesmatechatsdk.domain.usecases.GetConversationListUseCase
import com.rapidops.salesmatechatsdk.domain.usecases.GetUserFromUserIdUseCase
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.filterIsInstance
import kotlinx.coroutines.withContext
import javax.inject.Inject

internal class ConversationListViewModel @Inject constructor(
    private val appSettingsDataSource: IAppSettingsDataSource,
    private val coroutineContextProvider: ICoroutineContextProvider,
    private val getConversationUseCase: GetConversationListUseCase,
    private val getUserFromUserIdUseCase: GetUserFromUserIdUseCase
) : BaseViewModel(coroutineContextProvider) {

    val showConversationList = SingleLiveEvent<List<ConversationDetailItem>>()
    val updateConversationItem = SingleLiveEvent<ConversationDetailItem>()
    val updateConversationItemMessage = SingleLiveEvent<MessageItem>()
    val showLoadMore = SingleLiveEvent<Boolean>()

    companion object {
        private const val PAGE_SIZE = 20
    }

    fun subscribe() {
        loadConversationList()
        subscribeEvents()
    }


    val pingRes: PingRes by lazy {
        appSettingsDataSource.pingRes
    }

    fun loadConversationList(offSet: Int = 0) {
        progress.value = offSet == 0
        showLoadMore.value = offSet != 0
        withoutProgress({
            val conversationList =
                getConversationUseCase.execute(GetConversationListUseCase.Param(PAGE_SIZE, offSet))
            withContext(coroutineContextProvider.ui) {
                showConversationList.value = conversationList
                showLoadMore.value = false
                progress.value = false
            }
        }, {
            showLoadMore.value = false
            progress.value = false
            defaultErrorHandler(it)
        })
    }

    private fun subscribeEvents() {
        subscribeEvent {
            EventBus.events.filterIsInstance<AppEvent.UpdateConversationDetailEvent>()
                .collectLatest { updateConversationDetail ->
                    val conversationDetailItem = updateConversationDetail.data
                    updateConversationItem.value = conversationDetailItem
                }
        }
        subscribeEvent {
            EventBus.events.filterIsInstance<AppEvent.DeleteMessageEvent>()
                .collectLatest { deleteMessageEventDetail ->
                    updateConversationItemMessage.value = deleteMessageEventDetail.data
                }
        }
    }

}