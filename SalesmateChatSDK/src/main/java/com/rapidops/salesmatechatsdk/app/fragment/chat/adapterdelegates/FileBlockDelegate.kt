package com.rapidops.salesmatechatsdk.app.fragment.chat.adapterdelegates

import android.app.Activity
import android.view.View
import android.view.ViewGroup
import com.rapidops.salesmatechatsdk.app.extension.getResourceIdFromFileExtension
import com.rapidops.salesmatechatsdk.app.fragment.chat.adapter.MessageViewHolder
import com.rapidops.salesmatechatsdk.app.utils.ColorUtil
import com.rapidops.salesmatechatsdk.app.utils.ColorUtil.foregroundColor
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
        val viewHolder = holder as FileBlockViewHolder
        val fileBlockDataItem = items[position] as FileBlockDataItem

        val name = fileBlockDataItem.fileAttachmentData?.name ?: ""
        viewHolder.bind.txtFileName.text = name

        if (fileBlockDataItem.isSelfMessage) {
            viewHolder.bind.txtFileName.setTextColor(ColorUtil.actionColor.foregroundColor())
        }

        viewHolder.bind.imgFile.setImageResource(name.getResourceIdFromFileExtension())

    }

    override fun isForViewType(item: BlockDataItem, position: Int): Boolean {
        return item is FileBlockDataItem
    }

    internal class FileBlockViewHolder(itemView: View) : MessageViewHolder(itemView) {
        val bind = RFileBlockBinding.bind(itemView)

    }

}