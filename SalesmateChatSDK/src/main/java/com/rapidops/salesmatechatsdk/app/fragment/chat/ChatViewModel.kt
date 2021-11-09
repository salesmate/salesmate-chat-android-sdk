package com.rapidops.salesmatechatsdk.app.fragment.chat

import android.content.Context
import android.net.Uri
import android.os.CountDownTimer
import androidx.documentfile.provider.DocumentFile
import androidx.lifecycle.viewModelScope
import com.rapidops.salesmatechatsdk.app.base.BaseViewModel
import com.rapidops.salesmatechatsdk.app.coroutines.ICoroutineContextProvider
import com.rapidops.salesmatechatsdk.app.extension.DateUtil
import com.rapidops.salesmatechatsdk.app.extension.DateUtil.isCurrentWeekDay
import com.rapidops.salesmatechatsdk.app.extension.parseFromISOFormat
import com.rapidops.salesmatechatsdk.app.socket.SocketController
import com.rapidops.salesmatechatsdk.app.utils.AppEvent
import com.rapidops.salesmatechatsdk.app.utils.EventBus
import com.rapidops.salesmatechatsdk.app.utils.FileUtil.getFile
import com.rapidops.salesmatechatsdk.app.utils.FileUtil.isGifFile
import com.rapidops.salesmatechatsdk.app.utils.FileUtil.isImageFile
import com.rapidops.salesmatechatsdk.app.utils.FileUtil.isValidFileSize
import com.rapidops.salesmatechatsdk.app.utils.PlayType
import com.rapidops.salesmatechatsdk.app.utils.SingleLiveEvent
import com.rapidops.salesmatechatsdk.data.reqmodels.Blocks
import com.rapidops.salesmatechatsdk.data.reqmodels.SendMessageReq
import com.rapidops.salesmatechatsdk.data.reqmodels.convertToMessageItem
import com.rapidops.salesmatechatsdk.data.resmodels.PingRes
import com.rapidops.salesmatechatsdk.domain.datasources.IAppSettingsDataSource
import com.rapidops.salesmatechatsdk.domain.models.*
import com.rapidops.salesmatechatsdk.domain.models.message.*
import com.rapidops.salesmatechatsdk.domain.usecases.*
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.filterIsInstance
import kotlinx.coroutines.launch
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
    private val submitRemarkUseCase: SubmitRemarkUseCase,
    private val socketController: SocketController,
    private val submitContactUseCase: SubmitContactUseCase,
    private val trackEventUseCase: TrackEventUseCase,
    private val downloadTranscriptUseCase: DownloadTranscriptUseCase,
) : BaseViewModel(coroutineContextProvider) {

    companion object {
        private const val PAGE_SIZE = 50
        private const val TYPING_DEBOUNCE = 800L
        private const val ASK_EMAIL_TIMER: Long = (2 * 60 * 1000).toLong()
    }

    val canUploadAttachment: Boolean by lazy {
        pingRes.securitySettings?.canUploadAttachment ?: false
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
    val showGIFNotSupportMessageDialog = SingleLiveEvent<Nothing>()
    val showTypingMessageView = SingleLiveEvent<User>()
    val hideTypingMessageView = SingleLiveEvent<Nothing>()
    val showAskEmailView = SingleLiveEvent<Nothing>()
    val updateAskEmailMessage = SingleLiveEvent<Nothing>()
    val showExportedChatFile = SingleLiveEvent<File>()
    val updateConversationReadStatus = SingleLiveEvent<Nothing>()
    val showCloseConversationView = SingleLiveEvent<Nothing>()
    val playSoundForMessage = SingleLiveEvent<PlayType>()
    val showSendMessageView = SingleLiveEvent<Nothing>()

    private var conversationId: String? = null
    private var lastSendMessageId: String? = null

    private var isEmailAsked = false
    var isUserHasRead: Boolean = false
    private var isContactHasRead: Boolean = false

    fun subscribe(conversationId: String?, isLastMessageRead: Boolean = false) {
        isContactHasRead = isLastMessageRead
        this.conversationId = conversationId
        conversationId?.let {
            withProgress({
                val params = GetConversationDetailUseCase.Param(conversationId)
                val conversationDetailRes = getConversationDetailUseCase.execute(params)
                withContext(coroutineContextProvider.ui) {
                    showConversationDetail.value = conversationDetailRes
                    isUserHasRead = conversationDetailRes.conversations?.userHasRead == true
                    showInitialView(conversationDetailRes.conversations)
                }
                loadMessageList(conversationId)
            },{

            })

            loadReadConversationForVisitorIfRequired()
        } ?: run {
            showInitialView()
        }

        subscribeEvents()
    }

    private fun showInitialView(conversations: Conversations? = null) {
        if (isConversationOpen(conversations)) {
            if (shouldShowAskEmailView()) {
                showAskEmailView.call()
            } else {
                showSendMessageView.call()
            }
        } else {
            showCloseConversationView.call()
        }
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
        cancelableJobWithoutProgress({
            val params = GetMessageListUseCase.Param(conversationId, 10, 0, lastMessageDate)
            val response = getMessageListUseCase.execute(params).toMutableList()
            val filteredMessages = getFilteredMessages(response)
            withContext(coroutineContextProvider.ui) {
                playSound(PlayType.RECEIVE)
                showNewMessage.value = filteredMessages
            }
        }, {

        })

    }

    private fun getFilteredMessages(messageItem: List<MessageItem>): List<MessageItem> {
        if (messageItem.any { it.messageType == MessageType.EMAIL_ASKED.value }) {
            isEmailAsked = true
        }
        return messageItem.filter { item -> adapterMessageList.any { item.id == it.id }.not() }
    }

    fun loadMoreMessageList(offSet: Int) {
        withoutProgress({
            loadMessageList(getConversationId(), offSet)
        }, {

        })
    }


    private fun subscribeEvents() {
        subscribeEvent {
            EventBus.events.filterIsInstance<AppEvent.NewMessageEvent>()
                .collectLatest { event ->
                    val eventData = event.data
                    if (eventData.conversationId == getConversationId() && lastSendMessageId != eventData.messageId) {
                        isContactHasRead = false
                        adapterMessageList.firstOrNull()?.let { messageItem ->
                            loadMessageListByLastMessageDate(
                                getConversationId(),
                                messageItem.createdDate
                            )
                        }
                    }
                }
        }

        subscribeEvent {
            EventBus.events.filterIsInstance<AppEvent.UpdateConversationDetailEvent>()
                .collectLatest { updateConversationDetail ->
                    if (updateConversationDetail.data.conversations?.id == getConversationId()) {
                        val conversationDetailItem = updateConversationDetail.data
                        showConversationDetail.value = conversationDetailItem
                    }
                }
        }

        subscribeEvent {
            EventBus.events.filterIsInstance<AppEvent.ConversationRatingChangeEvent>()
                .collectLatest { ratingChangeData ->
                    if (ratingChangeData.conversationId == getConversationId()) {
                        updateRatingMessage.value = ratingChangeData.rating
                        showConversationDetail.value?.conversations?.rating =
                            ratingChangeData.rating
                    }
                }
        }

        subscribeEvent {
            EventBus.events.filterIsInstance<AppEvent.ConversationRemarkChangeEvent>()
                .collectLatest { remarkChangeData ->
                    if (remarkChangeData.conversationId == getConversationId()) {
                        updateRatingMessage.value = remarkChangeData.remark
                        showConversationDetail.value?.conversations?.remark =
                            remarkChangeData.remark
                    }
                }
        }

        subscribeEvent {
            EventBus.events.filterIsInstance<AppEvent.DeleteMessageEvent>()
                .collectLatest { deleteMessageDetail ->
                    deleteMessageDetail.data.user =
                        getUserFromUserIdUseCase.execute(deleteMessageDetail.data.userId)
                    if (deleteMessageDetail.data.conversationId == getConversationId()) {
                        updateMessage.value = deleteMessageDetail.data
                    }
                }
        }

        var hideTypingJob: Job? = null
        subscribeEvent {
            EventBus.events.filterIsInstance<AppEvent.TypingMessageEvent>()
                .collectLatest { typingMessageData ->
                    if (getConversationId() == typingMessageData.typingMessage.conversationId) {
                        val user =
                            getUserFromUserIdUseCase.execute(typingMessageData.typingMessage.userId)
                        showTypingMessageView.value = user
                        hideTypingJob?.cancel()
                        hideTypingJob = viewModelScope.launch {
                            delay(3000)
                            hideTypingMessageView.call()
                        }
                    }
                }
        }

        subscribeEvent {
            EventBus.events.filterIsInstance<AppEvent.ContactCreateEvent>()
                .collectLatest {
                    updateAskEmailMessage.call()
                }
        }

        subscribeEvent {
            EventBus.events.filterIsInstance<AppEvent.UserAvailabilityEvent>()
                .collectLatest { userAvailabilityEventData ->
                    showConversationDetail.value?.let {
                        if (userAvailabilityEventData.data.userIds.contains(it.user?.id)) {
                            it.user?.status = userAvailabilityEventData.data.status
                            showConversationDetail.value = it
                        }
                    }
                }
        }

        subscribeEvent {
            EventBus.events.filterIsInstance<AppEvent.ConversationHasReadEvent>()
                .collectLatest { conversationHasReadEventData ->
                    if (conversationHasReadEventData.conversationId == getConversationId()) {
                        isUserHasRead = conversationHasReadEventData.userHasRead
                        updateConversationReadStatus.call()
                    }
                }
        }

        subscribeEvent {
            EventBus.events.filterIsInstance<AppEvent.ConversationStatusUpdateEvent>()
                .collectLatest {
                    if (it.conversationId == getConversationId() && it.status == ConversationStatus.CLOSED.value) {
                        showCloseConversationView.call()
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
            isUserHasRead = false
            withContext(coroutineContextProvider.ui) {
                messageItem.sendStatus =
                    if (response.isSuccess) SendStatus.SUCCESS else SendStatus.FAIL
                playSound(if (response.isSuccess) PlayType.SEND else PlayType.FAIL)
                updateMessage.value = messageItem
                setAndResetTimerForAskEmailIfRequired()
            }
        }, {
            messageItem.sendStatus = SendStatus.FAIL
            playSound(PlayType.FAIL)
            updateMessage.value = messageItem
        })
    }

    private fun loadReadConversationForVisitorIfRequired() {
        if (isContactHasRead.not()) {
                isContactHasRead = true
                withoutProgress({
                    readConversationForVisitorUseCase.execute(
                        ReadConversationForVisitorUseCase.Param(getConversationId())
                    )
                }, {
                    isContactHasRead = false
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
                if (isSupportedFile(context, documentFile)) {
                    if (documentFile.length().isValidFileSize()) {
                        val messageItem = getNewAttachmentMessageItem(documentFile, context)
                        messageItem.sendStatus = SendStatus.SENDING
                        withContext(coroutineContextProvider.ui) {
                            showNewMessage.value = listOf(messageItem)
                        }
                        val file = documentFile.uri.getFile(context)
                        uploadFile(file, messageItem)
                    } else {
                        withContext(coroutineContextProvider.ui) {
                            showOverLimitFileMessageDialog.call()
                        }
                    }
                } else {
                    withContext(coroutineContextProvider.ui) {
                        showGIFNotSupportMessageDialog.call()
                    }
                }
            }
        }, {

        })
    }

    private fun isSupportedFile(context: Context, documentFile: DocumentFile): Boolean {
        return if (documentFile.isGifFile(context)) {
            pingRes.misc?.gifSupportEnabled == true
        } else {
            true
        }
    }

    private suspend fun uploadFile(file: File, messageItem: MessageItem) {
        try {
            val sendMessageReq =
                uploadFileUseCase.execute(UploadFileUseCase.Param(file, messageItem.id))

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
                playSound(PlayType.FAIL)
                updateMessage.value = messageItem
            }
        }
    }

    private fun getNewAttachmentMessageItem(
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

    fun sendTypingEvent() {
        cancelableJobWithoutProgress({
            delay(TYPING_DEBOUNCE)
            showConversationDetail.value?.conversations?.let {
                socketController.sendTypingEvent(it)
            }
        }, {})
        loadReadConversationForVisitorIfRequired()
    }

    private fun shouldShowAskEmailView(): Boolean {
        if (appSettingsDataSource.isContact.not()) {
            return when (pingRes.upfrontEmailCollection?.frequency) {
                FrequencyType.ALWAYS.value -> {
                    true
                }
                FrequencyType.ONLY_OUTSIDE_OF_OFFICE_HOURS.value -> {
                    isOnOfficeHours().not()
                }
                else -> {
                    false
                }
            }
        } else {
            return false
        }
    }

    private fun isOnOfficeHours(): Boolean {
        pingRes.availability?.officeHours?.let { officeHours ->
            officeHours.find { it.weekName.isCurrentWeekDay() }?.let {
                if (DateUtil.isTodayInBetween(it.startTime, it.endTime)) {
                    return true
                }
            }
            officeHours.find { it.weekName == DateUtil.getWeekDayTypeOfToday() }
                ?.let {
                    if (DateUtil.isTodayInBetween(it.startTime, it.endTime)) {
                        return true
                    }
                }
        }
        return false
    }

    fun submitContactDetail(name: String, email: String) {
        withProgress({
            submitContactUseCase.execute(SubmitContactUseCase.Param(getConversationId(), email))
            val sessionId = showConversationDetail.value?.conversations?.sessionId ?: ""
            trackEventUseCase.execute((TrackEventUseCase.Param(name, email, sessionId)))
            withContext(coroutineContextProvider.ui) {
                updateAskEmailMessage.call()
                showSendMessageView.call()
            }
        })
    }

    fun sendEmailAskedMessage() {
        val sendMessageReq = sendMessageUseCase.getNewSendMessageReq(isBot = true)
        sendMessageReq.blockData.apply {
            add(Blocks().apply {
                this.text = "Give the team a way to reach you:"
                this.type = BlockType.TEXT.value
            })
        }
        val messageItem = sendMessageReq.convertToMessageItem()
        messageItem.createdDate = DateUtil.getCurrentISOFormatDateTime()
        messageItem.sendStatus = SendStatus.NONE

        withoutProgress({
            isEmailAsked = true
            val response = sendMessageUseCase.execute(
                SendMessageUseCase.Param(
                    getConversationId(),
                    sendMessageReq
                )
            )
            if (response.isEmailAsked.not()) {
                val emailAskMessageReq = sendMessageUseCase.getNewSendMessageReq(isBot = true)
                emailAskMessageReq.messageType = MessageType.EMAIL_ASKED.value
                val emailAskMessageItem = emailAskMessageReq.convertToMessageItem()
                emailAskMessageItem.createdDate = DateUtil.getCurrentISOFormatDateTime()
                emailAskMessageItem.sendStatus = SendStatus.NONE
                sendMessageUseCase.execute(
                    SendMessageUseCase.Param(
                        getConversationId(),
                        emailAskMessageReq
                    )
                )
                lastSendMessageId = sendMessageReq.messageId
                withContext(coroutineContextProvider.ui) {
                    showNewMessage.value = listOf(emailAskMessageItem, messageItem)
                }
            }
        }, {
            isEmailAsked = false
        })
    }

    private var askForEmailTimer: CountDownTimer? = null
    private fun setAndResetTimerForAskEmailIfRequired() {
        askForEmailTimer?.cancel()
        if (requiredEmailAsked()) {
            askForEmailTimer =
                object : CountDownTimer(ASK_EMAIL_TIMER, 1000) {
                    override fun onTick(millisUntilFinished: Long) {

                    }

                    override fun onFinish() {
                        if (isEmailAsked) return
                        sendEmailAskedMessage()
                    }
                }
            askForEmailTimer?.start()
        }
    }

    private fun requiredEmailAsked(): Boolean {
        return appSettingsDataSource.isContact.not() && isEmailAsked.not()
    }

    fun downloadTranscript() {
        withProgress({
            val file = downloadTranscriptUseCase.execute(getConversationId())
            withContext(coroutineContextProvider.ui) {
                showExportedChatFile.value = file
            }
        })
    }

    private fun isConversationOpen(conversations: Conversations?): Boolean {
        conversations?.let {
            return when {
                it.status == ConversationStatus.OPEN.value -> {
                    true
                }
                appSettingsDataSource.preventRepliesToCloseConversations -> {
                    val closedDate = it.closedDate.parseFromISOFormat()
                    val nextToAvailableDate =
                        closedDate.plusDays(appSettingsDataSource.preventRepliesToCloseConversationsWithinNumberOfDays)
                    nextToAvailableDate.isAfterNow
                }
                else -> {
                    true
                }
            }
        }
        return true
    }

    private fun playSound(playType: PlayType) {
        if (pingRes.misc?.playSoundsForMessenger == true) {
            playSoundForMessage.value = playType
        }
    }
}