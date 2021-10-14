package com.rapidops.salesmatechatsdk.app.fragment.chat.adapter

import android.view.View
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import com.rapidops.salesmatechatsdk.R
import com.rapidops.salesmatechatsdk.app.base.BaseRecyclerViewAdapter
import com.rapidops.salesmatechatsdk.app.extension.loadCircleProfileImage
import com.rapidops.salesmatechatsdk.databinding.RChatTopBarUserItemBinding
import com.rapidops.salesmatechatsdk.domain.models.User

internal class ChatTopBarUserAdapter(private val availableUserCount: Int) :
    BaseRecyclerViewAdapter<User>() {
    override fun getRowLayoutId(viewType: Int): Int {
        return R.layout.r_chat_top_bar_user_item
    }

    override fun bind(viewHolder: RecyclerView.ViewHolder, position: Int, item: User) {
        val userViewHolder = viewHolder as ChatTopBarUserViewHolder
        userViewHolder.bindViewHolder(item)
    }

    override fun getViewHolder(view: View, viewType: Int): RecyclerView.ViewHolder {
        return ChatTopBarUserViewHolder(view)
    }

    inner class ChatTopBarUserViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        private val bind = RChatTopBarUserItemBinding.bind(itemView)
        fun bindViewHolder(item: User) {

            if (adapterPosition == 3) {
                bind.imgUser.isVisible = false
                bind.txtCount.isVisible = true
                val moreCount = availableUserCount - 3
                bind.txtCount.text = String.format("+%d", moreCount)
                bind.txtName.text = context.getString(R.string.lbl_more)
            } else {
                bind.imgUser.isVisible = true
                bind.txtCount.isVisible = false
                bind.imgUser.loadCircleProfileImage(item.profileUrl, item.firstName)
                bind.txtName.text = item.firstName
            }

        }
    }

}