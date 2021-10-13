package com.rapidops.salesmatechatsdk.app.fragment.chat.adapterdelegates

import android.app.Activity
import android.view.View
import android.view.ViewGroup
import com.rapidops.salesmatechatsdk.app.extension.loadCircleProfileImage
import com.rapidops.salesmatechatsdk.app.fragment.chat.adapter.MessageViewHolder
import com.rapidops.salesmatechatsdk.databinding.RIncomingPlainMessageBinding
import com.rapidops.salesmatechatsdk.domain.models.message.MessageItem

internal class IncomingPlainMessageDelegate(activity: Activity) :
    BaseMessageAdapterDelegate(activity) {
    override fun onCreateMessageHolder(parent: ViewGroup): MessageViewHolder {
        val view = RIncomingPlainMessageBinding.inflate(inflater, parent, false).root
        return IncomingPlainMessageViewHolder(view)
    }

    override fun onBindMessageViewHolder(
        items: List<MessageItem>,
        position: Int,
        holder: MessageViewHolder
    ) {
        val messageItem = items[position]
        val bind = RIncomingPlainMessageBinding.bind(holder.itemView)
        bind.imgUser.loadCircleProfileImage(
            messageItem.user?.profileUrl,
            messageItem.user?.firstName
        )
        bind.incPlainTextView.txtPlainMessage.text = messageItem.messageSummary

    }

    override fun isForViewType(item: MessageItem, position: Int): Boolean {
        return item.userId.isNotEmpty()
    }

    internal class IncomingPlainMessageViewHolder(itemView: View) : MessageViewHolder(itemView)

}