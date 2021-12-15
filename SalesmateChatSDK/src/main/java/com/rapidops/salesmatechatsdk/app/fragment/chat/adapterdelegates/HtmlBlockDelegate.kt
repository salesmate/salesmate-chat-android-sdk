package com.rapidops.salesmatechatsdk.app.fragment.chat.adapterdelegates

import android.app.Activity
import android.view.View
import android.view.ViewGroup
import com.rapidops.salesmatechatsdk.app.fragment.chat.adapter.MessageViewHolder
import com.rapidops.salesmatechatsdk.app.interfaces.MessageAdapterListener
import com.rapidops.salesmatechatsdk.app.utils.ColorUtil
import com.rapidops.salesmatechatsdk.app.utils.ColorUtil.foregroundColor
import com.rapidops.salesmatechatsdk.app.utils.FileUtil.isImageUrl
import com.rapidops.salesmatechatsdk.app.view.htmltextview.GlideImageGetter
import com.rapidops.salesmatechatsdk.databinding.RHtmlBlockBinding
import com.rapidops.salesmatechatsdk.domain.models.message.BlockDataItem
import com.rapidops.salesmatechatsdk.domain.models.message.HtmlBlockDataItem

internal class HtmlBlockDelegate(
    activity: Activity,
    private val messageAdapterListener: MessageAdapterListener? = null
) :
    BaseBlockAdapterDelegate(activity) {
    override fun onCreateMessageHolder(parent: ViewGroup): MessageViewHolder {
        val view = RHtmlBlockBinding.inflate(inflater, parent, false).root
        return HtmlBlockViewHolder(view)
    }

    override fun onBindMessageViewHolder(
        items: List<BlockDataItem>,
        position: Int,
        holder: MessageViewHolder
    ) {
        val viewHolder = holder as HtmlBlockViewHolder
        val htmlBlockDataItem = items[position] as HtmlBlockDataItem

        viewHolder.bind.txtHtmlMessage.setHtml(
            htmlBlockDataItem.text,
            GlideImageGetter(viewHolder.bind.txtHtmlMessage)
        )

        if (htmlBlockDataItem.isSelfMessage) {
            viewHolder.bind.txtHtmlMessage.setTextColor(ColorUtil.actionColor.foregroundColor())
        }

        viewHolder.bind.txtHtmlMessage.setOnClickATagListener { widget, spannedText, href ->
            if (href?.isImageUrl() == true) {
                messageAdapterListener?.onImageClicked(href)
                return@setOnClickATagListener true
            } else {
                return@setOnClickATagListener false
            }
        }
    }

    override fun isForViewType(item: BlockDataItem, position: Int): Boolean {
        return item is HtmlBlockDataItem
    }

    internal inner class HtmlBlockViewHolder(itemView: View) : MessageViewHolder(itemView) {
        val bind = RHtmlBlockBinding.bind(itemView)

    }

}