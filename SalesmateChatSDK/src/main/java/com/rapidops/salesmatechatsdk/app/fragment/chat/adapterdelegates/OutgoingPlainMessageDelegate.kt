package com.rapidops.salesmatechatsdk.app.fragment.chat.adapterdelegates

import android.app.Activity
import android.view.View
import android.view.ViewGroup
import com.rapidops.salesmatechatsdk.app.fragment.chat.adapter.MessageViewHolder
import com.rapidops.salesmatechatsdk.app.utils.ColorUtil
import com.rapidops.salesmatechatsdk.app.utils.ColorUtil.foregroundColor
import com.rapidops.salesmatechatsdk.app.utils.ColorUtil.updateActionTint
import com.rapidops.salesmatechatsdk.databinding.ROutgoingPlainMessageBinding
import com.rapidops.salesmatechatsdk.domain.models.message.MessageItem

internal class OutgoingPlainMessageDelegate(activity: Activity) :
    BaseMessageAdapterDelegate(activity) {
    override fun onCreateMessageHolder(parent: ViewGroup): MessageViewHolder {
        val view = ROutgoingPlainMessageBinding.inflate(inflater, parent, false).root
        return OutgoingPlainMessageViewHolder(view)
    }

    override fun onBindMessageViewHolder(
        items: List<MessageItem>,
        position: Int,
        holder: MessageViewHolder
    ) {
        val messageItem = items[position]
        val bind = ROutgoingPlainMessageBinding.bind(holder.itemView)
        bind.flBackground.updateActionTint()
        bind.incPlainTextView.txtPlainMessage.setTextColor(ColorUtil.actionColor.foregroundColor())
        bind.incPlainTextView.txtPlainMessage.text = messageItem.messageSummary
    }

    override fun isForViewType(item: MessageItem, position: Int): Boolean {
        return item.userId.isEmpty()
    }

    internal class OutgoingPlainMessageViewHolder(itemView: View) : MessageViewHolder(itemView)

}