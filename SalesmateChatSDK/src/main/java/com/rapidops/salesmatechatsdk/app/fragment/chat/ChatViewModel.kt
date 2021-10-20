package com.rapidops.salesmatechatsdk.app.fragment.chat

import com.rapidops.salesmatechatsdk.app.base.BaseViewModel
import com.rapidops.salesmatechatsdk.app.coroutines.ICoroutineContextProvider
import com.rapidops.salesmatechatsdk.app.utils.AppEvent
import com.rapidops.salesmatechatsdk.app.utils.EventBus
import com.rapidops.salesmatechatsdk.app.utils.SingleLiveEvent
import com.rapidops.salesmatechatsdk.data.resmodels.PingRes
import com.rapidops.salesmatechatsdk.domain.datasources.IAppSettingsDataSource
import com.rapidops.salesmatechatsdk.domain.models.ConversationDetailItem
import com.rapidops.salesmatechatsdk.domain.models.message.MessageItem
import com.rapidops.salesmatechatsdk.domain.usecases.GetConversationDetailUseCase
import com.rapidops.salesmatechatsdk.domain.usecases.GetMessageListUseCase
import com.rapidops.salesmatechatsdk.domain.usecases.GetUserFromUserIdUseCase
import kotlinx.coroutines.cancel
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.filterIsInstance
import kotlinx.coroutines.withContext
import javax.inject.Inject

internal class ChatViewModel @Inject constructor(
    private val appSettingsDataSource: IAppSettingsDataSource,
    private val coroutineContextProvider: ICoroutineContextProvider,
    private val getConversationDetailUseCase: GetConversationDetailUseCase,
    private val getMessageListUseCase: GetMessageListUseCase,
    private val getUserFromUserIdUseCase: GetUserFromUserIdUseCase
) : BaseViewModel(coroutineContextProvider) {

    var adapterMessageList: List<MessageItem> = listOf()

    val pingRes: PingRes by lazy {
        appSettingsDataSource.pingRes
    }

    companion object {
        private const val PAGE_SIZE = 50
    }

    val showConversationDetail = SingleLiveEvent<ConversationDetailItem>()
    val showMessageList = SingleLiveEvent<List<MessageItem>>()
    val showNewMessage = SingleLiveEvent<List<MessageItem>>()
    val deleteMessage = SingleLiveEvent<MessageItem>()

    var conversationId: String? = null


    fun subscribe(conversationId: String?) {
        conversationId?.let {
            this.conversationId = conversationId
            withProgress({
                val params = GetConversationDetailUseCase.Param(conversationId)
                val conversationDetailRes = getConversationDetailUseCase.execute(params)
                withContext(coroutineContextProvider.ui) {
                    showConversationDetail.value = conversationDetailRes
                }
                loadMessageList(it)

                subscribeEvent {
                    EventBus.events.filterIsInstance<AppEvent.NewMessageEvent>()
                        .collectLatest { event ->
                            adapterMessageList.firstOrNull()?.let {
                                loadMessageListByLastMessageDate(
                                    conversationId,
                                    it.createdDate
                                )
                            }

                        }
                }
            })
        }

        subscribeEvents()
    }

    private suspend fun loadMessageList(conversationId: String, offSet: Int = 0) {
        val params = GetMessageListUseCase.Param(conversationId, PAGE_SIZE, offSet)
        val response = getMessageListUseCase.execute(params).toMutableList()
        val filteredMessages = getFilteredMessages(response)
        withContext(coroutineContextProvider.ui) {
            showMessageList.value = filteredMessages
        }
    }

    private fun loadMessageListByLastMessageDate(
        conversationId: String,
        lastMessageDate: String
    ) {
        cancellableScope.cancel()
        cancelableJobWithoutProgress({
            val params = GetMessageListUseCase.Param(conversationId, 10, 0, lastMessageDate)
            val response = getMessageListUseCase.execute(params).toMutableList()
            val filteredMessages = getFilteredMessages(response)
            withContext(coroutineContextProvider.ui) {
                showNewMessage.value = filteredMessages
            }
        }, {

        })

    }

    private fun getFilteredMessages(messageItem: List<MessageItem>): List<MessageItem> {
        return messageItem.filter { item -> adapterMessageList.any { item.id == it.id }.not() }
    }

    fun loadMoreMessageList(offSet: Int) {
        conversationId?.let {
            withoutProgress({
                loadMessageList(it, offSet)
            }, {

            })
        }
    }


    private fun subscribeEvents() {

        subscribeEvent {
            EventBus.events.filterIsInstance<AppEvent.UpdateConversationDetailEvent>()
                .collectLatest { updateConversationDetail ->
                    if (updateConversationDetail.data.conversations?.id == conversationId) {
                        val conversationDetailItem = updateConversationDetail.data
                        showConversationDetail.value = conversationDetailItem
                    }
                }
        }

        subscribeEvent {
            EventBus.events.filterIsInstance<AppEvent.DeleteMessageEvent>()
                .collectLatest { deleteMessageDetail ->
                    deleteMessageDetail.data.user =
                        getUserFromUserIdUseCase.execute(deleteMessageDetail.data.userId)
                    if (deleteMessageDetail.data.conversationId == conversationId) {
                        deleteMessage.value = deleteMessageDetail.data
                    }
                }
        }

    }

    fun updateAdapterList(items: MutableList<MessageItem>?) {
        adapterMessageList = items ?: listOf()
    }

}