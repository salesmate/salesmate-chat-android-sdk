package com.rapidops.salesmatechatsdk.app.fragment.chat.adapterdelegates

import android.app.Activity
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.appcompat.widget.AppCompatTextView
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import com.rapidops.salesmatechatsdk.R
import com.rapidops.salesmatechatsdk.app.extension.getMessageTime
import com.rapidops.salesmatechatsdk.app.fragment.chat.adapter.MessageViewHolder
import com.rapidops.salesmatechatsdk.app.recyclerview.adapterdelegates.AdapterDelegate
import com.rapidops.salesmatechatsdk.domain.models.message.MessageItem

internal abstract class BaseMessageAdapterDelegate(activity: Activity) :
    AdapterDelegate<MutableList<MessageItem>>() {

    protected val inflater: LayoutInflater = activity.layoutInflater

    override fun onCreateViewHolder(parent: ViewGroup): RecyclerView.ViewHolder {
        return onCreateMessageHolder(parent)
    }

    override fun onBindViewHolder(
        items: MutableList<MessageItem>,
        position: Int,
        holder: RecyclerView.ViewHolder,
        payloads: MutableList<Any>
    ) {
        holder.setIsRecyclable(false)

        bindDateTimeView(holder, items, position)

        onBindMessageViewHolder(items, position, holder as MessageViewHolder)
    }

    override fun isForViewType(items: MutableList<MessageItem>, position: Int): Boolean {
        return isForViewType(items[position], position)
    }

    protected abstract fun onCreateMessageHolder(parent: ViewGroup): MessageViewHolder

    protected abstract fun onBindMessageViewHolder(
        items: List<MessageItem>,
        position: Int,
        holder: MessageViewHolder
    )

    protected abstract fun isForViewType(item: MessageItem, position: Int): Boolean

    private fun bindDateTimeView(
        holder: RecyclerView.ViewHolder,
        items: MutableList<MessageItem>,
        position: Int,
    ) {
        val messageItem = items[position]
        val txtDateTime = holder.itemView.findViewById<AppCompatTextView>(R.id.incDateTextView)
        txtDateTime?.apply {
            if (hideDateTimeView(items, position)) {
                text = ""
                isVisible = false
            } else {
                isVisible = true
                text = messageItem.createdDate.getMessageTime()
            }
        }
    }

    private fun hideDateTimeView(items: MutableList<MessageItem>, position: Int): Boolean {
        val isNotLast = position != items.lastIndex
        val isFirst = 0 == position
        if (!isNotLast || isFirst) return false
        val previousMessage = items[position - 1]
        val currentMessage = items[position]
        if (previousMessage.userId == currentMessage.userId) {
            val previousMessageDate = previousMessage.createdDate.getMessageTime()
            val currentMessageDate = currentMessage.createdDate.getMessageTime()
            return previousMessageDate == currentMessageDate
        } else {
            return false
        }
    }

}