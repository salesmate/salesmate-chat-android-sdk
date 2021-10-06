package com.rapidops.salesmatechatsdk.app.fragment.conversation_list

import android.view.View
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import com.rapidops.salesmatechatsdk.R
import com.rapidops.salesmatechatsdk.app.base.BaseRecyclerViewAdapter
import com.rapidops.salesmatechatsdk.app.extension.loadCircleProfileImage
import com.rapidops.salesmatechatsdk.app.utils.OverlapDecoration
import com.rapidops.salesmatechatsdk.databinding.RUserItemBinding
import com.rapidops.salesmatechatsdk.domain.models.User
import java.lang.Math.abs

internal class UserAdapter(private val availableUserCount: Int) : BaseRecyclerViewAdapter<User>() {
    override fun getRowLayoutId(viewType: Int): Int {
        return R.layout.r_user_item
    }

    override fun bind(viewHolder: RecyclerView.ViewHolder, position: Int, item: User) {
        val userViewHolder = viewHolder as UserViewHolder
        userViewHolder.bindViewHolder(item)
    }

    override fun getViewHolder(view: View, viewType: Int): RecyclerView.ViewHolder {
        return UserViewHolder(view)
    }

    inner class UserViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        private val bind = RUserItemBinding.bind(itemView)
        fun bindViewHolder(item: User) {
            if (adapterPosition == itemCount - 1) {
                (bind.root.layoutParams as RecyclerView.LayoutParams).setMargins(
                    0,
                    0,
                    abs(OverlapDecoration.VERT_OVERLAP),
                    0
                )
            }
            if (adapterPosition == 3) {
                bind.imgUser.isVisible = false
                bind.txtCount.isVisible = true
                val moreCount = availableUserCount - 3
                bind.txtCount.text = String.format("+%d", moreCount)
            } else {
                bind.imgUser.isVisible = true
                bind.txtCount.isVisible = false
                bind.imgUser.loadCircleProfileImage(item.profileUrl, item.firstName)
            }

        }
    }

}