package com.rapidops.salesmatechatsdk.app.fragment.chat

import android.content.Context
import android.net.Uri
import androidx.documentfile.provider.DocumentFile
import com.rapidops.salesmatechatsdk.app.base.BaseViewModel
import com.rapidops.salesmatechatsdk.app.coroutines.ICoroutineContextProvider
import com.rapidops.salesmatechatsdk.app.extension.DateUtil
import com.rapidops.salesmatechatsdk.app.utils.AppEvent
import com.rapidops.salesmatechatsdk.app.utils.EventBus
import com.rapidops.salesmatechatsdk.app.utils.FileUtil.getFile
import com.rapidops.salesmatechatsdk.app.utils.FileUtil.isImageFile
import com.rapidops.salesmatechatsdk.app.utils.FileUtil.isValidFileSize
import com.rapidops.salesmatechatsdk.app.utils.SingleLiveEvent
import com.rapidops.salesmatechatsdk.data.reqmodels.Blocks
import com.rapidops.salesmatechatsdk.data.reqmodels.SendMessageReq
import com.rapidops.salesmatechatsdk.data.reqmodels.convertToMessageItem
import com.rapidops.salesmatechatsdk.data.resmodels.PingRes
import com.rapidops.salesmatechatsdk.domain.datasources.IAppSettingsDataSource
import com.rapidops.salesmatechatsdk.domain.models.BlockType
import com.rapidops.salesmatechatsdk.domain.models.ConversationDetailItem
import com.rapidops.salesmatechatsdk.domain.models.message.*
import com.rapidops.salesmatechatsdk.domain.usecases.*
import kotlinx.coroutines.cancel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.filterIsInstance
import kotlinx.coroutines.withContext
import java.io.File
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
    private val uploadFileUseCase: UploadFileUseCase,
    private val submitRatingUseCase: SubmitRatingUseCase,
    private val submitRemarkUseCase: SubmitRemarkUseCase
) : BaseViewModel(coroutineContextProvider) {

    companion object {
        private const val PAGE_SIZE = 50
    }

    var adapterMessageList: List<MessageItem> = listOf()

    val pingRes: PingRes by lazy {
        appSettingsDataSource.pingRes
    }
    val showConversationDetail = SingleLiveEvent<ConversationDetailItem>()
    val showMessageList = SingleLiveEvent<List<MessageItem>>()
    val showNewMessage = SingleLiveEvent<List<MessageItem>>()
    val updateMessage = SingleLiveEvent<MessageItem>()
    val updateRatingMessage = SingleLiveEvent<String>()
    val showOverLimitFileMessageDialog = SingleLiveEvent<Nothing>()

    private var conversationId: String? = null
    private var lastSendMessageId: String? = null


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

    private fun getConversationId(): String {
        return conversationId ?: run {
            conversationId = UUID.randomUUID().toString()
            return conversationId!!
        }
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
                    val eventData = event.data
                    if (eventData.conversationId == conversationId && lastSendMessageId != eventData.messageId) {
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
            EventBus.events.filterIsInstance<AppEvent.ConversationRatingChangeEvent>()
                .collectLatest { ratingChangeData ->
                    if (ratingChangeData.conversationId == conversationId) {
                        updateRatingMessage.value = ratingChangeData.rating
                        showConversationDetail.value?.conversations?.rating = ratingChangeData.rating
                    }
                }
        }

        subscribeEvent {
            EventBus.events.filterIsInstance<AppEvent.ConversationRemarkChangeEvent>()
                .collectLatest { remarkChangeData ->
                    if (remarkChangeData.conversationId == conversationId) {
                        updateRatingMessage.value = remarkChangeData.remark
                        showConversationDetail.value?.conversations?.remark = remarkChangeData.remark
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
        val sendMessageReq = sendMessageUseCase.getNewSendMessageReq()
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
            delay(100)
            val response = sendMessageUseCase.execute(
                SendMessageUseCase.Param(
                    getConversationId(),
                    sendMessageReq
                )
            )
            lastSendMessageId = sendMessageReq.messageId
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
        if (messageItem.sendStatus == SendStatus.UPLOADING_FAIL) {
            retryUploadAndSendMessage(messageItem)
        } else {
            messageItem.createdDate = DateUtil.getCurrentISOFormatDateTime()
            messageItem.sendStatus = SendStatus.SENDING
            updateMessage.value = messageItem
            val sendMessageReq = messageItem.convertToSendMessageReq()
                .apply {
                    conversationName = appSettingsDataSource.contactName
                }
            sendMessage(sendMessageReq, messageItem)
        }
    }

    fun sendAttachment(context: Context, uri: Uri) {
        withoutProgress({
            DocumentFile.fromSingleUri(context, uri)?.let { documentFile ->
                if (documentFile.length().isValidFileSize()) {
                    val messageItem = getNewMessageItem(documentFile, context)
                    messageItem.sendStatus = SendStatus.SENDING
                    withContext(coroutineContextProvider.ui) {
                        showNewMessage.value = listOf(messageItem)
                    }
                    val file = documentFile.uri.getFile(context)
                    uploadFile(file, messageItem)
                } else {
                    showOverLimitFileMessageDialog.call()
                }
            }
        }, {

        })
    }

    private suspend fun uploadFile(file: File, messageItem: MessageItem) {
        try {
            val sendMessageReq = uploadFileUseCase.execute(UploadFileUseCase.Param(file, messageItem.id))

            val updatedMessageItem = sendMessageReq.convertToMessageItem()
            updatedMessageItem.createdDate = DateUtil.getCurrentISOFormatDateTime()

            val blockData = updatedMessageItem.blockData.first()
            if (blockData is ImageBlockDataItem) {
                blockData.fileAttachmentData?.url = file.path
            } else if (blockData is FileBlockDataItem) {
                blockData.fileAttachmentData?.url = file.path
            }
            withContext(coroutineContextProvider.ui) {
                updateMessage.value = updatedMessageItem
            }

            sendMessage(sendMessageReq, updatedMessageItem)
        } catch (e: Exception) {
            delay(100)
            withContext(coroutineContextProvider.ui) {
                val blockData = messageItem.blockData.first()
                if (blockData is ImageBlockDataItem) {
                    blockData.fileAttachmentData?.url = file.path
                } else if (blockData is FileBlockDataItem) {
                    blockData.fileAttachmentData?.url = file.path
                }
                messageItem.sendStatus = SendStatus.UPLOADING_FAIL
                updateMessage.value = messageItem
            }
        }
    }

    private fun getNewMessageItem(
        documentFile: DocumentFile,
        context: Context
    ): MessageItem {
        val messageItem = MessageItem()
        messageItem.apply {
            this.id = UUID.randomUUID().toString()
            this.messageType = MessageType.COMMENT.value
            this.createdDate = DateUtil.getCurrentISOFormatDateTime()
            val blockDataItem =
                if (documentFile.isImageFile(context)) {
                    ImageBlockDataItem().apply {
                        blockType = BlockType.IMAGE
                        fileAttachmentData = FileAttachmentData().apply {
                            this.url = documentFile.uri.toString()
                            this.contentType = documentFile.type ?: ""
                            this.name = documentFile.name ?: ""
                        }
                    }
                } else {
                    FileBlockDataItem().apply {
                        blockType = BlockType.FILE
                        fileAttachmentData = FileAttachmentData().apply {
                            this.url = documentFile.uri.toString()
                            this.contentType = documentFile.type ?: ""
                            this.name = documentFile.name ?: ""
                        }
                    }
                }
            blockDataItem.isSelfMessage = true
            this.blockData.add(blockDataItem)
        }
        return messageItem
    }

    private fun retryUploadAndSendMessage(messageItem: MessageItem) {
        messageItem.sendStatus = SendStatus.SENDING
        updateMessage.value = messageItem
        withoutProgress({
            val filePath = when (val blockData = messageItem.blockData.first()) {
                is ImageBlockDataItem -> {
                    blockData.fileAttachmentData?.url
                }
                is FileBlockDataItem -> {
                    blockData.fileAttachmentData?.url
                }
                else -> {
                    null
                }
            }
            if (!filePath.isNullOrEmpty()) {
                uploadFile(File(filePath), messageItem)
            }
        }, {

        })
    }

    fun submitRating(rating: String) {
        withoutProgress({
            submitRatingUseCase.execute(SubmitRatingUseCase.Param(getConversationId(), rating))
        })
    }

    fun submitRemark(remark: String) {
        withoutProgress({
            submitRemarkUseCase.execute(SubmitRemarkUseCase.Param(getConversationId(), remark))
        })
    }
}