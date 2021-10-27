package com.rapidops.salesmatechatsdk.app.fragment.chat.adapterdelegates

import android.app.Activity
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.rapidops.salesmatechatsdk.app.extension.loadCircleProfileImage
import com.rapidops.salesmatechatsdk.app.fragment.chat.adapter.BlockAdapter
import com.rapidops.salesmatechatsdk.app.fragment.chat.adapter.MessageViewHolder
import com.rapidops.salesmatechatsdk.app.view.SpacesItemDecoration
import com.rapidops.salesmatechatsdk.databinding.RIncomingMessageBinding
import com.rapidops.salesmatechatsdk.domain.models.message.MessageItem
import com.rapidops.salesmatechatsdk.domain.models.message.MessageType


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
        val viewHolder = holder as IncomingMessageViewHolder
        val messageItem = items[position]
        viewHolder.bind.imgUser.loadCircleProfileImage(
            messageItem.user?.profileUrl,
            messageItem.user?.firstName
        )
        viewHolder.blockAdapter.setItemList(messageItem.blockData)
    }

    override fun isForViewType(item: MessageItem, position: Int): Boolean {
        return item.userId.isNotEmpty() && item.messageType == MessageType.COMMENT.value && !item.isBot
    }

    internal inner class IncomingMessageViewHolder(itemView: View) : MessageViewHolder(itemView) {
        val blockAdapter = BlockAdapter(activity)
        val bind = RIncomingMessageBinding.bind(itemView)
        val layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)

        init {
            bind.rvBlockList.addItemDecoration(SpacesItemDecoration(8))
            bind.rvBlockList.layoutManager = layoutManager
            bind.rvBlockList.adapter = blockAdapter

        }
    }

}