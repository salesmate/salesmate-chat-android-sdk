package com.rapidops.salesmatechatsdk.app.fragment.chat.adapter

import android.app.Activity
import com.rapidops.salesmatechatsdk.app.fragment.chat.adapterdelegates.IncomingMessageDelegate
import com.rapidops.salesmatechatsdk.app.fragment.chat.adapterdelegates.OutgoingMessageDelegate
import com.rapidops.salesmatechatsdk.app.interfaces.MessageAdapterListener
import com.rapidops.salesmatechatsdk.app.recyclerview.adapterdelegates.ListDelegationAdapter
import com.rapidops.salesmatechatsdk.domain.models.message.MessageItem

internal open class MessageAdapter(
    activity: Activity,
    messageAdapterListener: MessageAdapterListener
) :
    ListDelegationAdapter<MutableList<MessageItem>>() {
    init {
        // Delegates
        delegatesManager.addDelegate(IncomingMessageDelegate(activity))
        delegatesManager.addDelegate(OutgoingMessageDelegate(activity, messageAdapterListener))
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
        this.items.addAll(0, items)
        notifyItemRangeInserted(0, items.size)
    }


    fun updateMessage(item: MessageItem) {
        val indexOfFirst = items.indexOfFirst { item.id == it.id }
        if (indexOfFirst != -1) {
            items[indexOfFirst] = item
            notifyItemChanged(indexOfFirst)
        }
    }

}