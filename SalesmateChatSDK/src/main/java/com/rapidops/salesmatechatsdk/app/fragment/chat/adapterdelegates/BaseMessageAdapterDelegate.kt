package com.rapidops.salesmatechatsdk.app.fragment.chat.adapterdelegates

import android.app.Activity
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.rapidops.salesmatechatsdk.app.fragment.chat.adapter.MessageViewHolder
import com.rapidops.salesmatechatsdk.app.recyclerview.adapterdelegates.AdapterDelegate

abstract class BaseMessageAdapterDelegate(activity: Activity) : AdapterDelegate<List<String>>() {

    protected val inflater: LayoutInflater = activity.layoutInflater

    override fun onCreateViewHolder(parent: ViewGroup): RecyclerView.ViewHolder {
        return onCreateMessageHolder(parent)
    }

    override fun onBindViewHolder(
        items: List<String>,
        position: Int,
        holder: RecyclerView.ViewHolder,
        payloads: MutableList<Any>
    ) {
        onBindMessageViewHolder(items, position, holder as MessageViewHolder)
    }


    protected abstract fun onCreateMessageHolder(parent: ViewGroup): MessageViewHolder

    protected abstract fun onBindMessageViewHolder(
        items: List<String>,
        position: Int,
        holder: MessageViewHolder
    )
}