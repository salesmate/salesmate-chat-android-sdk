package com.rapidops.salesmatechatsdk.app.fragment.chat.adapter

import android.app.Activity
import com.rapidops.salesmatechatsdk.app.fragment.chat.adapterdelegates.IncomingPlainMessageDelegate
import com.rapidops.salesmatechatsdk.app.fragment.chat.adapterdelegates.OutgoingPlainMessageDelegate
import com.rapidops.salesmatechatsdk.app.recyclerview.adapterdelegates.ListDelegationAdapter

class MessageV1Adapter(activity: Activity) : ListDelegationAdapter<List<String>>() {
    init {
        // Delegates
        delegatesManager.addDelegate(IncomingPlainMessageDelegate(activity))
        delegatesManager.addDelegate(OutgoingPlainMessageDelegate(activity))

        items = mutableListOf(
            "hello",
            "hello",
            "hello",
            "hello",
            "hello",
            "hello",
            "hello",
            "hello",
            "hello",
            "hello",
            "hello",
            "hello",
            "hello",
            "hello",
            "hello",
            "hello",
            "hello",
            "hello",
            "hello",
            "hello",
            "hello",
            "hello",
            "hello",
            "hello"
        )
    }
}