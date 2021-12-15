package com.rapidops.salesmatechatsdk.app.fragment.chat.adapter

import android.app.Activity
import com.rapidops.salesmatechatsdk.app.fragment.chat.adapterdelegates.*
import com.rapidops.salesmatechatsdk.app.interfaces.MessageAdapterListener
import com.rapidops.salesmatechatsdk.app.recyclerview.adapterdelegates.ListDelegationAdapter
import com.rapidops.salesmatechatsdk.domain.models.message.BlockDataItem

internal class BlockAdapter(
    activity: Activity,
    messageAdapterListener: MessageAdapterListener? = null
) :
    ListDelegationAdapter<MutableList<BlockDataItem>>() {
    init {
        // Delegates
        delegatesManager.addDelegate(TextBlockDelegate(activity))
        delegatesManager.addDelegate(ImageBlockDelegate(activity, messageAdapterListener))
        delegatesManager.addDelegate(FileBlockDelegate(activity, messageAdapterListener))
        delegatesManager.addDelegate(HtmlBlockDelegate(activity, messageAdapterListener))
        delegatesManager.addDelegate(DeleteBlockDelegate(activity))

    }


    fun setItemList(items: MutableList<BlockDataItem>) {
        this.items = items
        notifyItemRangeInserted(0, items.size)
    }

}