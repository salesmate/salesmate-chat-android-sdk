package com.rapidops.salesmatechatsdk.app.fragment.chat.adapterdelegates

import android.app.Activity
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.rapidops.salesmatechatsdk.app.fragment.chat.adapter.MessageViewHolder
import com.rapidops.salesmatechatsdk.app.interfaces.MessageAdapterListener
import com.rapidops.salesmatechatsdk.databinding.RFallbackMessageBinding
import com.rapidops.salesmatechatsdk.domain.models.message.MessageItem


internal class FallbackMessageDelegate(
    private val activity: Activity,
    private val messageAdapterListener: MessageAdapterListener
) :
    BaseMessageAdapterDelegate(activity, messageAdapterListener) {
    override fun onCreateMessageHolder(parent: ViewGroup): MessageViewHolder {
        val view = RFallbackMessageBinding.inflate(inflater, parent, false).root
        return FallbackMessageViewHolder(view)
    }

    override fun onBindMessageViewHolder(
        items: List<MessageItem>,
        position: Int,
        holder: MessageViewHolder
    ) {
        val viewHolder = holder as FallbackMessageViewHolder
        val messageItem = items[position]

    }

    override fun isForViewType(item: MessageItem, position: Int): Boolean {
        return true
    }

    internal inner class FallbackMessageViewHolder(itemView: View) : MessageViewHolder(itemView) {
        val bind = RFallbackMessageBinding.bind(itemView)
        val layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
    }

}