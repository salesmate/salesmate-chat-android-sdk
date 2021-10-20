package com.rapidops.salesmatechatsdk.app.fragment.chat.adapterdelegates

import android.app.Activity
import android.view.View
import android.view.ViewGroup
import com.rapidops.salesmatechatsdk.app.extension.loadImageWithRoundedTransformation
import com.rapidops.salesmatechatsdk.app.fragment.chat.adapter.MessageViewHolder
import com.rapidops.salesmatechatsdk.databinding.RImageBlockBinding
import com.rapidops.salesmatechatsdk.domain.models.message.BlockDataItem
import com.rapidops.salesmatechatsdk.domain.models.message.ImageBlockDataItem

internal class ImageBlockDelegate(activity: Activity) :
    BaseBlockAdapterDelegate(activity) {
    override fun onCreateMessageHolder(parent: ViewGroup): MessageViewHolder {
        val view = RImageBlockBinding.inflate(inflater, parent, false).root
        return ImageBlockViewHolder(view)
    }

    override fun onBindMessageViewHolder(
        items: List<BlockDataItem>,
        position: Int,
        holder: MessageViewHolder
    ) {
        val imageBlock = items[position] as ImageBlockDataItem
        val viewHolder = holder as ImageBlockViewHolder
        viewHolder.bind.imgBlockImage.loadImageWithRoundedTransformation(imageBlock.fileAttachmentData?.url)

    }

    override fun isForViewType(item: BlockDataItem, position: Int): Boolean {
        return item is ImageBlockDataItem
    }

    internal class ImageBlockViewHolder(itemView: View) : MessageViewHolder(itemView){
        val bind = RImageBlockBinding.bind(itemView)

    }

}