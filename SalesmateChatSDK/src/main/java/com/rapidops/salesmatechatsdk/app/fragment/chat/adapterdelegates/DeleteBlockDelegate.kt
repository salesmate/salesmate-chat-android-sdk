package com.rapidops.salesmatechatsdk.app.fragment.chat.adapterdelegates

import android.app.Activity
import android.view.View
import android.view.ViewGroup
import com.rapidops.salesmatechatsdk.app.fragment.chat.adapter.MessageViewHolder
import com.rapidops.salesmatechatsdk.app.utils.ColorUtil
import com.rapidops.salesmatechatsdk.app.utils.ColorUtil.foregroundColor
import com.rapidops.salesmatechatsdk.app.view.htmltextview.PicassoImageGetter
import com.rapidops.salesmatechatsdk.databinding.RDeleteBlockBinding
import com.rapidops.salesmatechatsdk.domain.models.message.BlockDataItem
import com.rapidops.salesmatechatsdk.domain.models.message.DeleteBlockDataItem

internal class DeleteBlockDelegate(activity: Activity) :
    BaseBlockAdapterDelegate(activity) {
    override fun onCreateMessageHolder(parent: ViewGroup): MessageViewHolder {
        val view = RDeleteBlockBinding.inflate(inflater, parent, false).root
        return DeleteBlockViewHolder(view)
    }

    override fun onBindMessageViewHolder(
        items: List<BlockDataItem>,
        position: Int,
        holder: MessageViewHolder
    ) {
        val viewHolder = holder as DeleteBlockViewHolder
        val blockItem = items[position] as DeleteBlockDataItem

        viewHolder.bind.txtDeleteMessage.setHtml(
            blockItem.text,
            PicassoImageGetter(
                viewHolder.bind.txtDeleteMessage
            )
        )

        if (blockItem.isSelfMessage) {
            viewHolder.bind.txtDeleteMessage.setTextColor(ColorUtil.actionColor.foregroundColor())
        }
    }

    override fun isForViewType(item: BlockDataItem, position: Int): Boolean {
        return item is DeleteBlockDataItem
    }

    internal class DeleteBlockViewHolder(itemView: View) : MessageViewHolder(itemView) {
        val bind = RDeleteBlockBinding.bind(itemView)

    }

}