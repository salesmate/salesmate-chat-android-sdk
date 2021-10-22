package com.rapidops.salesmatechatsdk.app.fragment.chat.adapterdelegates

import android.app.Activity
import android.view.View
import android.view.ViewGroup
import com.rapidops.salesmatechatsdk.app.fragment.chat.adapter.MessageViewHolder
import com.rapidops.salesmatechatsdk.app.utils.ColorUtil
import com.rapidops.salesmatechatsdk.app.utils.ColorUtil.foregroundColor
import com.rapidops.salesmatechatsdk.app.view.htmltextview.PicassoImageGetter
import com.rapidops.salesmatechatsdk.databinding.RTextBlockBinding
import com.rapidops.salesmatechatsdk.domain.models.message.BlockDataItem
import com.rapidops.salesmatechatsdk.domain.models.message.TextBlockDataItem

internal class TextBlockDelegate(activity: Activity) :
    BaseBlockAdapterDelegate(activity) {
    override fun onCreateMessageHolder(parent: ViewGroup): MessageViewHolder {
        val view = RTextBlockBinding.inflate(inflater, parent, false).root
        return TextBlockViewHolder(view)
    }

    override fun onBindMessageViewHolder(
        items: List<BlockDataItem>,
        position: Int,
        holder: MessageViewHolder
    ) {
        val viewHolder = holder as TextBlockViewHolder
        val blockItem = items[position] as TextBlockDataItem

        viewHolder.bind.txtPlainMessage.setHtml(
            blockItem.text ?: "",
            PicassoImageGetter(
                viewHolder.bind.txtPlainMessage
            )
        )

        if (blockItem.isSelfMessage) {
            viewHolder.bind.txtPlainMessage.setTextColor(ColorUtil.actionColor.foregroundColor())
        }
    }

    override fun isForViewType(item: BlockDataItem, position: Int): Boolean {
        return item is TextBlockDataItem
    }

    internal class TextBlockViewHolder(itemView: View) : MessageViewHolder(itemView) {
        val bind = RTextBlockBinding.bind(itemView)

    }

}