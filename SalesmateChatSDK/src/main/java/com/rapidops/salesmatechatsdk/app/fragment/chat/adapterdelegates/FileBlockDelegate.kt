package com.rapidops.salesmatechatsdk.app.fragment.chat.adapterdelegates

import android.app.Activity
import android.view.View
import android.view.ViewGroup
import com.rapidops.salesmatechatsdk.R
import com.rapidops.salesmatechatsdk.app.fragment.chat.adapter.MessageViewHolder
import com.rapidops.salesmatechatsdk.app.utils.ColorUtil
import com.rapidops.salesmatechatsdk.app.utils.ColorUtil.foregroundColor
import com.rapidops.salesmatechatsdk.app.utils.ColorUtil.setTintAction
import com.rapidops.salesmatechatsdk.databinding.RFileBlockBinding
import com.rapidops.salesmatechatsdk.domain.models.message.BlockDataItem
import com.rapidops.salesmatechatsdk.domain.models.message.FileBlockDataItem

internal class FileBlockDelegate(activity: Activity) :
    BaseBlockAdapterDelegate(activity) {
    override fun onCreateMessageHolder(parent: ViewGroup): MessageViewHolder {
        val view = RFileBlockBinding.inflate(inflater, parent, false).root
        return FileBlockViewHolder(view)
    }

    override fun onBindMessageViewHolder(
        items: List<BlockDataItem>,
        position: Int,
        holder: MessageViewHolder
    ) {
        val fileBlockDataItem = items[position] as FileBlockDataItem
        val bind = RFileBlockBinding.bind(holder.itemView)

        bind.txtFileName.text = fileBlockDataItem.fileAttachmentData?.name

        if (fileBlockDataItem.isSelfMessage) {
            bind.txtFileName.setTextColor(ColorUtil.actionColor.foregroundColor())
        }

        val drawable = holder.getDrawable(R.drawable.ic_attachment)
        drawable?.setTintAction()
        bind.txtFileName.setCompoundDrawablesRelativeWithIntrinsicBounds(drawable, null, null, null)

    }

    override fun isForViewType(item: BlockDataItem, position: Int): Boolean {
        return item is FileBlockDataItem
    }

    internal class FileBlockViewHolder(itemView: View) : MessageViewHolder(itemView)

}