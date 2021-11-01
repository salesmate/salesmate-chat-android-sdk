package com.rapidops.salesmatechatsdk.app.fragment.chat.adapterdelegates

import android.app.Activity
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.rapidops.salesmatechatsdk.app.fragment.chat.adapter.BlockAdapter
import com.rapidops.salesmatechatsdk.app.fragment.chat.adapter.MessageViewHolder
import com.rapidops.salesmatechatsdk.app.interfaces.MessageAdapterListener
import com.rapidops.salesmatechatsdk.app.view.SpacesItemDecoration
import com.rapidops.salesmatechatsdk.databinding.RBotMessageBinding
import com.rapidops.salesmatechatsdk.domain.models.message.MessageItem
import com.rapidops.salesmatechatsdk.domain.models.message.MessageType


internal class BotMessageDelegate(
    private val activity: Activity,
    private val messageAdapterListener: MessageAdapterListener
) :
    BaseMessageAdapterDelegate(activity, messageAdapterListener) {
    override fun onCreateMessageHolder(parent: ViewGroup): MessageViewHolder {
        val view = RBotMessageBinding.inflate(inflater, parent, false).root
        return BotMessageViewHolder(view)
    }

    override fun onBindMessageViewHolder(
        items: List<MessageItem>,
        position: Int,
        holder: MessageViewHolder
    ) {
        val viewHolder = holder as BotMessageViewHolder
        val messageItem = items[position]
        viewHolder.blockAdapter.setItemList(messageItem.blockData)
    }

    override fun isForViewType(item: MessageItem, position: Int): Boolean {
        return item.messageType == MessageType.COMMENT.value && item.isBot
    }

    internal inner class BotMessageViewHolder(itemView: View) : MessageViewHolder(itemView) {
        val blockAdapter = BlockAdapter(activity)
        val bind = RBotMessageBinding.bind(itemView)
        val layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)

        init {
            bind.rvBlockList.addItemDecoration(SpacesItemDecoration(8))
            bind.rvBlockList.layoutManager = layoutManager
            bind.rvBlockList.adapter = blockAdapter

        }
    }

}