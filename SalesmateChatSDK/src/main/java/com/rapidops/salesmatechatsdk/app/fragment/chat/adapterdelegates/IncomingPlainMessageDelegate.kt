package com.rapidops.salesmatechatsdk.app.fragment.chat.adapterdelegates

import android.app.Activity
import android.view.View
import android.view.ViewGroup
import com.rapidops.salesmatechatsdk.app.fragment.chat.adapter.MessageViewHolder
import com.rapidops.salesmatechatsdk.databinding.RIncomingPlainMessageBinding

class IncomingPlainMessageDelegate(activity: Activity) :
    BaseMessageAdapterDelegate(activity) {
    override fun onCreateMessageHolder(parent: ViewGroup): MessageViewHolder {
        val view = RIncomingPlainMessageBinding.inflate(inflater, parent, false).root
        return IncomingPlainMessageViewHolder(view)
    }

    override fun onBindMessageViewHolder(
        items: List<String>,
        position: Int,
        holder: MessageViewHolder
    ) {
        val bind = RIncomingPlainMessageBinding.bind(holder.itemView)
        bind.incPlainTextView.txtPlainMessage.text =
            String.format("Position :- %s", position)
        bind.incDateTextView.txtDateTime.text = "Jul 23, 7:00 AM"
    }

    override fun isForViewType(items: List<String>, position: Int): Boolean {
        return position % 2 != 0
    }

    internal class IncomingPlainMessageViewHolder(itemView: View) : MessageViewHolder(itemView)

}