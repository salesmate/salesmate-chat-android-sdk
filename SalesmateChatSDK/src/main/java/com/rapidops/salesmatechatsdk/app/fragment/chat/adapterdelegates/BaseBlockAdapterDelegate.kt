package com.rapidops.salesmatechatsdk.app.fragment.chat.adapterdelegates

import android.app.Activity
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.rapidops.salesmatechatsdk.app.fragment.chat.adapter.MessageViewHolder
import com.rapidops.salesmatechatsdk.app.recyclerview.adapterdelegates.AdapterDelegate
import com.rapidops.salesmatechatsdk.domain.models.message.BlockDataItem

internal abstract class BaseBlockAdapterDelegate(activity: Activity) :
    AdapterDelegate<MutableList<BlockDataItem>>() {

    protected val inflater: LayoutInflater = activity.layoutInflater

    override fun onCreateViewHolder(parent: ViewGroup): RecyclerView.ViewHolder {
        return onCreateMessageHolder(parent)
    }

    override fun onBindViewHolder(
        items: MutableList<BlockDataItem>,
        position: Int,
        holder: RecyclerView.ViewHolder,
        payloads: MutableList<Any>
    ) {

        onBindMessageViewHolder(items, position, holder as MessageViewHolder)
    }

    override fun isForViewType(items: MutableList<BlockDataItem>, position: Int): Boolean {
        return isForViewType(items[position], position)
    }

    protected abstract fun onCreateMessageHolder(parent: ViewGroup): MessageViewHolder

    protected abstract fun onBindMessageViewHolder(
        items: List<BlockDataItem>,
        position: Int,
        holder: MessageViewHolder
    )

    protected abstract fun isForViewType(item: BlockDataItem, position: Int): Boolean
}