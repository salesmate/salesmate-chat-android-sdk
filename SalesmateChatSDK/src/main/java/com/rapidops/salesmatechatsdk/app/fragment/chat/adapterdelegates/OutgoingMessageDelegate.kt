package com.rapidops.salesmatechatsdk.app.fragment.chat.adapterdelegates

import android.app.Activity
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.recyclerview.widget.LinearLayoutManager
import com.rapidops.salesmatechatsdk.app.fragment.chat.adapter.BlockAdapter
import com.rapidops.salesmatechatsdk.app.fragment.chat.adapter.MessageViewHolder
import com.rapidops.salesmatechatsdk.app.interfaces.MessageAdapterListener
import com.rapidops.salesmatechatsdk.app.utils.ColorUtil.updateActionTint
import com.rapidops.salesmatechatsdk.app.view.SpacesItemDecoration
import com.rapidops.salesmatechatsdk.databinding.ROutgoingMessageBinding
import com.rapidops.salesmatechatsdk.domain.models.message.MessageItem
import com.rapidops.salesmatechatsdk.domain.models.message.SendStatus

internal class OutgoingMessageDelegate(
    private val activity: Activity,
    private val messageAdapterListener: MessageAdapterListener
) :
    BaseMessageAdapterDelegate(activity) {
    override fun onCreateMessageHolder(parent: ViewGroup): MessageViewHolder {
        val view = ROutgoingMessageBinding.inflate(inflater, parent, false).root
        return OutgoingMessageViewHolder(view)
    }

    override fun onBindMessageViewHolder(
        items: List<MessageItem>,
        position: Int,
        holder: MessageViewHolder
    ) {
        val viewHolder = holder as OutgoingMessageViewHolder
        val messageItem = items[position]

        viewHolder.blockAdapter.setItemList(messageItem.blockData)

        viewHolder.bind.txtFailStatus.apply {
            isVisible = messageItem.sendStatus == SendStatus.FAIL
        }
        viewHolder.bind.apply {
            txtFailStatus.isVisible = messageItem.sendStatus == SendStatus.FAIL
            imgInfo.isVisible = messageItem.sendStatus == SendStatus.FAIL
        }

        viewHolder.bind.imgInfo.setOnClickListener {
            messageAdapterListener.onInfoClick(messageItem)
        }

    }

    override fun isForViewType(item: MessageItem, position: Int): Boolean {
        return item.userId.isEmpty()
    }

    internal inner class OutgoingMessageViewHolder(itemView: View) : MessageViewHolder(itemView) {
        val blockAdapter = BlockAdapter(activity)
        val bind = ROutgoingMessageBinding.bind(itemView)
        val layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)

        init {
            bind.rvBlockList.addItemDecoration(SpacesItemDecoration(8))
            bind.rvBlockList.layoutManager = layoutManager
            bind.rvBlockList.updateActionTint()
            bind.rvBlockList.adapter = blockAdapter
        }
    }

}