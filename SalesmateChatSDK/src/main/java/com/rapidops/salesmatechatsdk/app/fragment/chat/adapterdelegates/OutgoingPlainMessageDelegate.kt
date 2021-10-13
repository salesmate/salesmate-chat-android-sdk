package com.rapidops.salesmatechatsdk.app.fragment.chat.adapterdelegates

import android.app.Activity
import android.view.View
import android.view.ViewGroup
import com.rapidops.salesmatechatsdk.app.fragment.chat.adapter.MessageViewHolder
import com.rapidops.salesmatechatsdk.app.utils.ColorUtil
import com.rapidops.salesmatechatsdk.app.utils.ColorUtil.foregroundColor
import com.rapidops.salesmatechatsdk.app.utils.ColorUtil.updateActionTint
import com.rapidops.salesmatechatsdk.databinding.ROutgoingPlainMessageBinding

class OutgoingPlainMessageDelegate(activity: Activity) :
    BaseMessageAdapterDelegate(activity) {
    override fun onCreateMessageHolder(parent: ViewGroup): MessageViewHolder {
        val view = ROutgoingPlainMessageBinding.inflate(inflater, parent, false).root
        return OutgoingPlainMessageViewHolder(view)
    }

    override fun onBindMessageViewHolder(
        items: List<String>,
        position: Int,
        holder: MessageViewHolder
    ) {
        val bind = ROutgoingPlainMessageBinding.bind(holder.itemView)
        bind.flBackground.updateActionTint()
        bind.incPlainTextView.txtPlainMessage.setTextColor(ColorUtil.actionColor.foregroundColor())
        bind.incPlainTextView.txtPlainMessage.text =
            String.format("Position :- %s", position)
        bind.incDateTextView.txtDateTime.text = "Jul 23, 7:00 AM"
    }

    override fun isForViewType(items: List<String>, position: Int): Boolean {
        return position % 2 == 0
    }

    internal class OutgoingPlainMessageViewHolder(itemView: View) : MessageViewHolder(itemView)

}