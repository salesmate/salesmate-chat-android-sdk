package com.rapidops.salesmatechatsdk.app.fragment.recent_list.adapter

import android.view.View
import androidx.core.view.isInvisible
import androidx.recyclerview.widget.RecyclerView
import com.rapidops.salesmatechatsdk.R
import com.rapidops.salesmatechatsdk.app.base.LoadMoreBaseRecyclerViewAdapter
import com.rapidops.salesmatechatsdk.app.extension.getPeriod
import com.rapidops.salesmatechatsdk.app.extension.loadCircleProfileImage
import com.rapidops.salesmatechatsdk.app.utils.AppEvent
import com.rapidops.salesmatechatsdk.databinding.RConversationItemBinding
import com.rapidops.salesmatechatsdk.domain.models.ConversationDetailItem
import com.rapidops.salesmatechatsdk.domain.models.message.MessageItem
import java.util.*

internal class ConversationAdapter : LoadMoreBaseRecyclerViewAdapter<ConversationDetailItem>() {
    override fun getRowLayoutId(viewType: Int): Int {
        return R.layout.r_conversation_item
    }

    override fun bind(
        viewHolder: RecyclerView.ViewHolder,
        position: Int,
        item: ConversationDetailItem
    ) {
        val userViewHolder = viewHolder as ConversationViewHolder
        userViewHolder.bindViewHolder(item)
    }

    override fun getViewHolder(view: View, viewType: Int): RecyclerView.ViewHolder {
        return ConversationViewHolder(view)
    }

    override fun getViewType(position: Int): Int {
        return R.layout.r_conversation_item
    }

    inner class ConversationViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        private val bind = RConversationItemBinding.bind(itemView)
        fun bindViewHolder(item: ConversationDetailItem) {

            bind.imgStatus.isInvisible = item.isContactHasRead

            bind.imgConversation.loadCircleProfileImage(
                item.user?.profileUrl,
                item.user?.firstName
            )
            bind.txtConversationName.text =
                String.format("%s %s", item.user?.firstName, item.user?.lastName)

            bind.txtLastMessage.text = item.conversations?.lastMessageData?.messageSummary

            bind.txtTime.text =
                item.conversations?.lastMessageDate?.getPeriod()

            bind.root.setOnClickListener {
                clickListener?.onItemClick(adapterPosition, item)
            }
        }
    }

    fun updateConversationMessage(messageItem: MessageItem) {
        val list = getItems()
        val indexOfFirst =
            list.indexOfFirst { it.conversations?.id == messageItem.conversationId }
        if (indexOfFirst != 1) {
            val conversationDetailItem = list[indexOfFirst]
            conversationDetailItem.conversations?.lastMessageData?.messageSummary =
                messageItem.messageSummary
            notifyItemChanged(indexOfFirst)
        }
    }

    fun updateConversation(conversationDetailItem: ConversationDetailItem) {
        val list = getItems()
        val indexOfFirst =
            list.indexOfFirst { it.conversations?.id == conversationDetailItem.conversations?.id }
        if (indexOfFirst != -1) {
            list[indexOfFirst] = conversationDetailItem
            notifyItemChanged(indexOfFirst)
            Collections.swap(list, indexOfFirst, 0)
            notifyItemMoved(indexOfFirst, 0)
        } else {
            list.add(0, conversationDetailItem)
            notifyItemInserted(0)
        }
    }

    fun updateReadStatus(conversationHasReadEvent: AppEvent.ConversationHasReadEvent) {
        val list = getItems()
        val indexOfFirst =
            list.indexOfFirst { it.conversations?.id == conversationHasReadEvent.conversationId }
        if (indexOfFirst != -1) {
            list[indexOfFirst].conversations?.contactHasRead =
                conversationHasReadEvent.contactHasRead
            notifyItemChanged(indexOfFirst)
        }
    }

}