package com.rapidops.salesmatechatsdk.app.fragment.chat.adapter

import android.view.View
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import com.rapidops.salesmatechatsdk.R
import com.rapidops.salesmatechatsdk.app.base.BaseRecyclerViewAdapter
import com.rapidops.salesmatechatsdk.app.extension.loadCircleProfileImage
import com.rapidops.salesmatechatsdk.app.utils.OverlapDecoration
import com.rapidops.salesmatechatsdk.databinding.RChatToolbarUserItemBinding
import com.rapidops.salesmatechatsdk.domain.models.User
import kotlin.math.abs

internal class ToolbarUserAdapter(private val availableUserCount: Int) :
    BaseRecyclerViewAdapter<User>() {
    override fun getRowLayoutId(viewType: Int): Int {
        return R.layout.r_chat_toolbar_user_item
    }

    override fun bind(viewHolder: RecyclerView.ViewHolder, position: Int, item: User) {
        val userViewHolder = viewHolder as ToolbarUserViewHolder
        userViewHolder.bindViewHolder(item)
    }

    override fun getViewHolder(view: View, viewType: Int): RecyclerView.ViewHolder {
        return ToolbarUserViewHolder(view)
    }

    inner class ToolbarUserViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        private val bind = RChatToolbarUserItemBinding.bind(itemView)
        fun bindViewHolder(item: User) {
            if (adapterPosition == itemCount - 1) {
                (bind.root.layoutParams as RecyclerView.LayoutParams).setMargins(
                    0,
                    0,
                    abs(OverlapDecoration.VERT_OVERLAP),
                    0
                )
            }
            if (adapterPosition == 2) {
                bind.imgUser.isVisible = false
                bind.txtCount.isVisible = true
                val moreCount = availableUserCount - 2
                bind.txtCount.text = String.format("+%d", moreCount)
            } else {
                bind.imgUser.isVisible = true
                bind.txtCount.isVisible = false
                bind.imgUser.loadCircleProfileImage(item.profileUrl, item.firstName)
            }

        }
    }

}