package com.rapidops.salesmatechatsdk.app.fragment.chat

import com.rapidops.salesmatechatsdk.app.base.BaseViewModel
import com.rapidops.salesmatechatsdk.app.coroutines.ICoroutineContextProvider
import com.rapidops.salesmatechatsdk.app.utils.SingleLiveEvent
import com.rapidops.salesmatechatsdk.data.resmodels.PingRes
import com.rapidops.salesmatechatsdk.domain.datasources.IAppSettingsDataSource
import com.rapidops.salesmatechatsdk.domain.models.ConversationDetailItem
import com.rapidops.salesmatechatsdk.domain.models.message.MessageItem
import com.rapidops.salesmatechatsdk.domain.usecases.GetConversationDetailUseCase
import com.rapidops.salesmatechatsdk.domain.usecases.GetMessageListUseCase
import kotlinx.coroutines.withContext
import javax.inject.Inject

internal class ChatViewModel @Inject constructor(
    private val appSettingsDataSource: IAppSettingsDataSource,
    private val coroutineContextProvider: ICoroutineContextProvider,
    private val getConversationDetailUseCase: GetConversationDetailUseCase,
    private val getMessageListUseCase: GetMessageListUseCase,
) : BaseViewModel(coroutineContextProvider) {

    val pingRes: PingRes by lazy {
        appSettingsDataSource.pingRes
    }

    companion object {
        private const val PAGE_SIZE = 50
    }

    val showConversationDetail = SingleLiveEvent<ConversationDetailItem>()
    val showMessageList = SingleLiveEvent<List<MessageItem>>()

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
            })
        }

    }

    private suspend fun loadMessageList(conversationId: String, offSet: Int = 0) {
        val params = GetMessageListUseCase.Param(conversationId, PAGE_SIZE, offSet)
        val response = getMessageListUseCase.execute(params)
        withContext(coroutineContextProvider.ui) {
            showMessageList.value = response
        }
    }

    fun loadMoreMessageList(offSet: Int) {
        conversationId?.let {
            withoutProgress({
                loadMessageList(it, offSet)
            }, {

            })
        }
    }

}