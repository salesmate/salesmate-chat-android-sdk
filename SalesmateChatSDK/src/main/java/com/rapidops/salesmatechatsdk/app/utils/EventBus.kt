package com.rapidops.salesmatechatsdk.app.utils

import com.rapidops.salesmatechatsdk.domain.models.ConversationDetailItem
import com.rapidops.salesmatechatsdk.domain.models.UserAvailability
import com.rapidops.salesmatechatsdk.domain.models.events.ChatNewMessage
import com.rapidops.salesmatechatsdk.domain.models.events.TypingMessage
import com.rapidops.salesmatechatsdk.domain.models.message.MessageItem
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch

object EventBus {
    private const val TAG = "EventBus"

    private val _events = MutableSharedFlow<Any>()
    val events = _events.asSharedFlow()

    private suspend fun emitEvent(event: Any) {
        _events.emit(event)
    }

    private var eventScope = CoroutineScope(Job() + Dispatchers.Main)
    fun fireEvent(appEvent: Any) {
        eventScope.launch {
            emitEvent(appEvent)
        }
    }
}


internal sealed class AppEvent {

    object EventWithoutData : AppEvent()

    data class UserAvailabilityEvent(val data: UserAvailability) : AppEvent()
    data class NewMessageEvent(val data: ChatNewMessage) : AppEvent()
    data class UpdateConversationListEvent(val conversationId: String) : AppEvent()
    data class DeleteMessageEvent(val data: MessageItem) : AppEvent()
    data class ConversationRatingChangeEvent(val conversationId: String, val rating: String) :
        AppEvent()

    data class ConversationRemarkChangeEvent(val conversationId: String, val remark: String) :
        AppEvent()

    data class TypingMessageEvent(val typingMessage: TypingMessage) :
        AppEvent()

    object ContactCreateEvent : AppEvent()

    data class UpdateConversationDetailEvent(val data: ConversationDetailItem) : AppEvent()

    data class ConversationHasReadEvent(
        val conversationId: String,
        val userHasRead: Boolean,
        val contactHasRead: Boolean
    ) : AppEvent()

    data class ConversationStatusUpdateEvent(
        val conversationId: String,
        val status: String,
    ) : AppEvent()

}