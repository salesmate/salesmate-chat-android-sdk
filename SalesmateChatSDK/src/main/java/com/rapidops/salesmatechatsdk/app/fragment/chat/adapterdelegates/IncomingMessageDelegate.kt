package com.rapidops.salesmatechatsdk.app.fragment.chat.adapterdelegates

import android.app.Activity
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.rapidops.salesmatechatsdk.app.extension.loadCircleProfileImage
import com.rapidops.salesmatechatsdk.app.fragment.chat.adapter.BlockAdapter
import com.rapidops.salesmatechatsdk.app.fragment.chat.adapter.MessageViewHolder
import com.rapidops.salesmatechatsdk.databinding.RIncomingMessageBinding
import com.rapidops.salesmatechatsdk.domain.models.message.MessageItem

internal class IncomingMessageDelegate(private val activity: Activity) :
    BaseMessageAdapterDelegate(activity) {
    override fun onCreateMessageHolder(parent: ViewGroup): MessageViewHolder {
        val view = RIncomingMessageBinding.inflate(inflater, parent, false).root
        return IncomingMessageViewHolder(view)
    }

    override fun onBindMessageViewHolder(
        items: List<MessageItem>,
        position: Int,
        holder: MessageViewHolder
    ) {
        val messageItem = items[position]
        val bind = RIncomingMessageBinding.bind(holder.itemView)
        bind.imgUser.loadCircleProfileImage(
            messageItem.user?.profileUrl,
            messageItem.user?.firstName
        )

        val blockAdapter = BlockAdapter(activity, messageItem.blockData)
        val layoutManager =
            LinearLayoutManager(holder.context, LinearLayoutManager.VERTICAL, false)
        bind.rvBlockList.layoutManager = layoutManager
        bind.rvBlockList.adapter = blockAdapter
    }

    override fun isForViewType(item: MessageItem, position: Int): Boolean {
        return item.userId.isNotEmpty()
    }

    internal class IncomingMessageViewHolder(itemView: View) : MessageViewHolder(itemView)

}