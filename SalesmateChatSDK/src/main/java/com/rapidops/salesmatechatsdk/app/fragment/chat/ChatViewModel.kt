package com.rapidops.salesmatechatsdk.app.fragment.chat

import com.rapidops.salesmatechatsdk.app.base.BaseViewModel
import com.rapidops.salesmatechatsdk.app.coroutines.ICoroutineContextProvider
import com.rapidops.salesmatechatsdk.app.extension.DateUtil
import com.rapidops.salesmatechatsdk.app.utils.AppEvent
import com.rapidops.salesmatechatsdk.app.utils.EventBus
import com.rapidops.salesmatechatsdk.app.utils.SingleLiveEvent
import com.rapidops.salesmatechatsdk.data.reqmodels.Blocks
import com.rapidops.salesmatechatsdk.data.reqmodels.SendMessageReq
import com.rapidops.salesmatechatsdk.data.reqmodels.convertToMessageItem
import com.rapidops.salesmatechatsdk.data.resmodels.PingRes
import com.rapidops.salesmatechatsdk.domain.datasources.IAppSettingsDataSource
import com.rapidops.salesmatechatsdk.domain.models.BlockType
import com.rapidops.salesmatechatsdk.domain.models.ConversationDetailItem
import com.rapidops.salesmatechatsdk.domain.models.message.MessageItem
import com.rapidops.salesmatechatsdk.domain.models.message.MessageType
import com.rapidops.salesmatechatsdk.domain.models.message.SendStatus
import com.rapidops.salesmatechatsdk.domain.models.message.convertToSendMessageReq
import com.rapidops.salesmatechatsdk.domain.usecases.*
import kotlinx.coroutines.cancel
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.filterIsInstance
import kotlinx.coroutines.withContext
import java.util.*
import javax.inject.Inject

internal class ChatViewModel @Inject constructor(
    private val appSettingsDataSource: IAppSettingsDataSource,
    private val coroutineContextProvider: ICoroutineContextProvider,
    private val getConversationDetailUseCase: GetConversationDetailUseCase,
    private val getMessageListUseCase: GetMessageListUseCase,
    private val getUserFromUserIdUseCase: GetUserFromUserIdUseCase,
    private val sendMessageUseCase: SendMessageUseCase,
    private val readConversationForVisitorUseCase: ReadConversationForVisitorUseCase,
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
    val updateMessage = SingleLiveEvent<MessageItem>()

    private var conversationId: String? = null


    fun subscribe(conversationId: String?, isLastMessageRead: Boolean = false) {
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

            if (isLastMessageRead.not()) {
                loadReadConversationForVisitor()
            }
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
            EventBus.events.filterIsInstance<AppEvent.NewMessageEvent>()
                .collectLatest { event ->
                    if (event.data.conversationId == conversationId) {
                        conversationId?.let { conversationId ->
                            adapterMessageList.firstOrNull()?.let { messageItem ->
                                loadMessageListByLastMessageDate(
                                    conversationId,
                                    messageItem.createdDate
                                )
                            }
                        }
                    }
                }
        }

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
                        updateMessage.value = deleteMessageDetail.data
                    }
                }
        }

    }

    fun updateAdapterList(items: MutableList<MessageItem>?) {
        adapterMessageList = items ?: listOf()
    }

    fun sendTextMessage(typedMessage: String) {
        if (conversationId == null) {
            conversationId = UUID.randomUUID().toString()
        }
        val sendMessageReq = getNewSendMessageRes()
        sendMessageReq.blockData.apply {
            add(Blocks().apply {
                this.text = typedMessage
                this.type = BlockType.TEXT.value
            })
        }
        val messageItem = sendMessageReq.convertToMessageItem()
        messageItem.createdDate = DateUtil.getCurrentISOFormatDateTime()
        messageItem.sendStatus = SendStatus.SENDING
        showNewMessage.value = listOf(messageItem)
        sendMessage(sendMessageReq, messageItem)
    }

    private fun sendMessage(sendMessageReq: SendMessageReq, messageItem: MessageItem) {
        withoutProgress({
            val response = sendMessageUseCase.execute(
                SendMessageUseCase.Param(
                    conversationId!!,
                    sendMessageReq
                )
            )
            withContext(coroutineContextProvider.ui) {
                messageItem.sendStatus =
                    if (response.isSuccess) SendStatus.SUCCESS else SendStatus.FAIL
                updateMessage.value = messageItem
            }
        }, {
            messageItem.sendStatus = SendStatus.FAIL
            updateMessage.value = messageItem
        })
    }

    private fun loadReadConversationForVisitor() {
        conversationId?.let { conversationId ->
            withoutProgress({
                readConversationForVisitorUseCase.execute(
                    ReadConversationForVisitorUseCase.Param(
                        conversationId
                    )
                )
            }, {

            })
        }
    }

    fun onRetrySendMessage(messageItem: MessageItem) {
        messageItem.createdDate = DateUtil.getCurrentISOFormatDateTime()
        messageItem.sendStatus = SendStatus.SENDING
        updateMessage.value = messageItem
        sendMessage(messageItem.convertToSendMessageReq(), messageItem)
    }

    private fun getNewSendMessageRes(): SendMessageReq {
        val uniqueMessageId = UUID.randomUUID().toString()
        val sendMessageReq = SendMessageReq()
        sendMessageReq.apply {
            this.messageType = MessageType.COMMENT.value
            this.messageId = uniqueMessageId
            this.isBot = false
            this.isInbound = true
            this.conversationName = appSettingsDataSource.pseudoName
        }
        return sendMessageReq
    }
}