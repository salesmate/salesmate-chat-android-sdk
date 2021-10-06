package com.rapidops.salesmatechatsdk.app.fragment.recent_list.adapter

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.rapidops.salesmatechatsdk.R
import com.rapidops.salesmatechatsdk.app.base.BaseRecyclerViewAdapter
import com.rapidops.salesmatechatsdk.app.extension.getPeriod
import com.rapidops.salesmatechatsdk.app.extension.loadCircleProfileImage
import com.rapidops.salesmatechatsdk.databinding.RConversationItemBinding
import com.rapidops.salesmatechatsdk.domain.models.ConversationDetailItem

internal class ConversationAdapter : BaseRecyclerViewAdapter<ConversationDetailItem>() {
    override fun getRowLayoutId(viewType: Int): Int {
        return R.layout.r_conversation_item
    }

    override fun bind(
        viewHolder: RecyclerView.ViewHolder,
        position: Int,
        item: ConversationDetailItem
    ) {
        val userViewHolder = viewHolder as UserViewHolder
        userViewHolder.bindViewHolder(item)
    }

    override fun getViewHolder(view: View, viewType: Int): RecyclerView.ViewHolder {
        return UserViewHolder(view)
    }

    inner class UserViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        private val bind = RConversationItemBinding.bind(itemView)
        fun bindViewHolder(item: ConversationDetailItem) {
            item.user?.let {
                bind.imgConversation.loadCircleProfileImage(
                    item.user?.profileUrl,
                    item.user?.firstName
                )
                bind.txtConversationName.text =
                    String.format("%s %s", item.user?.firstName, item.user?.lastName)
            } ?: run {
                val name = "Mobile App"
                bind.imgConversation.loadCircleProfileImage(null, name)
                bind.txtConversationName.text = name
            }

            bind.txtLastMessage.text = item.conversations?.lastMessageData?.messageSummary

            bind.txtTime.text =
                item.conversations?.lastMessageDate?.getPeriod()

        }
    }

}