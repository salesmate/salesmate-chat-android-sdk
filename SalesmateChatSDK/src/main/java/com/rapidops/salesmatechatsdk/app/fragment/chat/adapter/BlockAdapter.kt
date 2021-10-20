package com.rapidops.salesmatechatsdk.app.fragment.chat.adapter

import android.app.Activity
import com.rapidops.salesmatechatsdk.app.fragment.chat.adapterdelegates.FileBlockDelegate
import com.rapidops.salesmatechatsdk.app.fragment.chat.adapterdelegates.HtmlBlockDelegate
import com.rapidops.salesmatechatsdk.app.fragment.chat.adapterdelegates.ImageBlockDelegate
import com.rapidops.salesmatechatsdk.app.fragment.chat.adapterdelegates.TextBlockDelegate
import com.rapidops.salesmatechatsdk.app.recyclerview.adapterdelegates.ListDelegationAdapter
import com.rapidops.salesmatechatsdk.domain.models.message.BlockDataItem
import com.rapidops.salesmatechatsdk.domain.models.message.MessageItem

internal class BlockAdapter(activity: Activity) :
    ListDelegationAdapter<MutableList<BlockDataItem>>() {
    init {
        // Delegates
        delegatesManager.addDelegate(TextBlockDelegate(activity))
        delegatesManager.addDelegate(ImageBlockDelegate(activity))
        delegatesManager.addDelegate(FileBlockDelegate(activity))
        delegatesManager.addDelegate(HtmlBlockDelegate(activity))

    }


    fun setItemList(items: MutableList<BlockDataItem>) {
        this.items = items
        notifyItemRangeInserted(0, items.size)
    }

}