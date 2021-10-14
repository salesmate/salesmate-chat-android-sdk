package com.rapidops.salesmatechatsdk.app.fragment.chat.adapter

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.rapidops.salesmatechatsdk.R
import com.rapidops.salesmatechatsdk.app.base.BaseRecyclerViewAdapter
import com.rapidops.salesmatechatsdk.app.utils.ColorUtil
import com.rapidops.salesmatechatsdk.app.utils.ColorUtil.foregroundColor
import com.rapidops.salesmatechatsdk.app.utils.ColorUtil.updateActionTint
import com.rapidops.salesmatechatsdk.databinding.RIncomingPlainMessageBinding
import com.rapidops.salesmatechatsdk.databinding.ROutgoingPlainMessageBinding

class MessageAdapter : BaseRecyclerViewAdapter<String>() {


    override fun getRowLayoutId(viewType: Int): Int {
        return viewType
    }

    override fun bind(viewHolder: RecyclerView.ViewHolder, position: Int, item: String) {
        if (position % 2 == 0) {
            (viewHolder as IncomingMessageViewHolder).bindViewHolder(item)
        } else {
            (viewHolder as OutgoingMessageViewHolder).bindViewHolder(item)
        }

    }

    override fun getViewHolder(view: View, viewType: Int): RecyclerView.ViewHolder {
        return if (viewType == R.layout.r_incoming_plain_message) {
            IncomingMessageViewHolder(view)
        } else {
            OutgoingMessageViewHolder(view)
        }

    }

    override fun getItemViewType(position: Int): Int {
        return if (position % 2 == 0) {
            R.layout.r_incoming_plain_message
        } else {
            R.layout.r_outgoing_plain_message
        }
    }


    inner class OutgoingMessageViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        private val bind = ROutgoingPlainMessageBinding.bind(itemView)
        fun bindViewHolder(item: String) {

            bind.flBackground.updateActionTint()
            bind.incPlainTextView.txtPlainMessage.text =
                String.format("Position :- %s", adapterPosition)
            bind.incDateTextView.txtDateTime.text = "Jul 23, 7:00 AM"
        }
    }

    inner class IncomingMessageViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        private val bind = RIncomingPlainMessageBinding.bind(itemView)
        fun bindViewHolder(item: String) {
            bind.incPlainTextView.txtPlainMessage.setTextColor(ColorUtil.actionColor.foregroundColor())
            bind.incPlainTextView.txtPlainMessage.text =
                String.format("Position :- %s", adapterPosition)
            bind.incDateTextView.txtDateTime.text = "Jul 23, 7:00 AM"
        }
    }
}