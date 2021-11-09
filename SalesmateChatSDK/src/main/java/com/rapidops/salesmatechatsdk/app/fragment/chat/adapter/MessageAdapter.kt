package com.rapidops.salesmatechatsdk.app.fragment.chat.adapter

import android.app.Activity
import com.rapidops.salesmatechatsdk.app.fragment.chat.adapterdelegates.*
import com.rapidops.salesmatechatsdk.app.interfaces.MessageAdapterListener
import com.rapidops.salesmatechatsdk.app.recyclerview.adapterdelegates.ListDelegationAdapter
import com.rapidops.salesmatechatsdk.core.SalesmateChat
import com.rapidops.salesmatechatsdk.domain.models.message.MessageItem
import com.rapidops.salesmatechatsdk.domain.models.message.MessageType

internal open class MessageAdapter(
    activity: Activity,
    messageAdapterListener: MessageAdapterListener
) :
    ListDelegationAdapter<MutableList<MessageItem>>() {

    val appSettingsDataSource = SalesmateChat.daggerDataComponent.getAppSettingsDataSource()

    init {
        // Delegates
        delegatesManager.addDelegate(IncomingMessageDelegate(activity, messageAdapterListener))
        delegatesManager.addDelegate(OutgoingMessageDelegate(activity, messageAdapterListener))
        delegatesManager.addDelegate(
            RatingAskMessageDelegate(
                activity,
                messageAdapterListener,
                appSettingsDataSource.pingRes.emojiMapping
            )
        )
        delegatesManager.addDelegate(
            EmailAskMessageDelegate(activity, messageAdapterListener) {
                appSettingsDataSource.contactData
            }
        )
        delegatesManager.addDelegate(BotMessageDelegate(activity, messageAdapterListener))
        delegatesManager.fallbackDelegate =
            FallbackMessageDelegate(activity, messageAdapterListener)
    }

    fun addItems(items: MutableList<MessageItem>) {
        if (items.isNotEmpty()) {
            val previousSize = itemCount
            if (this.items.addAll(items)) {
                notifyItemRangeInserted(previousSize, items.size)
            }
        }
    }

    fun setItemList(items: MutableList<MessageItem>) {
        this.items = items
        notifyItemRangeInserted(0, items.size)
    }

    fun addNewItems(items: MutableList<MessageItem>) {
        this.items?.let {
            this.items.addAll(0, items)
            notifyItemRangeInserted(0, items.size)
            if (this.items.size > 1) {
                notifyItemChanged(1)
            }
        }
    }


    fun updateMessage(item: MessageItem) {
        items?.let {
            val indexOfFirst = items.indexOfFirst { item.id == it.id }
            if (indexOfFirst != -1) {
                items[indexOfFirst] = item
                notifyItemChanged(indexOfFirst)
            }
        }
    }

    fun updateRatingMessage() {
        items?.let {
            val indexOfRatingMessage =
                items.indexOfFirst { it.messageType == MessageType.RATING_ASKED.value }
            notifyItemChanged(indexOfRatingMessage)
        }
    }

    fun updateAskEmailMessage() {
        items?.let {
            val indexOfAskEmailMessage =
                items.indexOfFirst { it.messageType == MessageType.EMAIL_ASKED.value }
            if (indexOfAskEmailMessage != -1) {
                items[indexOfAskEmailMessage].isEmailSubmitted = true
                notifyItemChanged(indexOfAskEmailMessage)
            }
        }
    }

    fun updateMessages() {
        notifyDataSetChanged()
    }

}