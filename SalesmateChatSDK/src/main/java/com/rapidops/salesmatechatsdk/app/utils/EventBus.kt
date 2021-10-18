package com.rapidops.salesmatechatsdk.app.utils

import com.rapidops.salesmatechatsdk.domain.models.ChatNewMessage
import com.rapidops.salesmatechatsdk.domain.models.ConversationDetailItem
import com.rapidops.salesmatechatsdk.domain.models.UserAvailability
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

    data class UpdateConversationDetailEvent(val data: ConversationDetailItem) : AppEvent()

}