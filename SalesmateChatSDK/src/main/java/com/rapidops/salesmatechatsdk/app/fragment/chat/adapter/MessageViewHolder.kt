package com.rapidops.salesmatechatsdk.app.fragment.chat.adapter

import android.content.Context
import android.graphics.drawable.Drawable
import android.view.View
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView

abstract class MessageViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

    val context: Context = itemView.context
    //Getter Methods for Resources
    fun getString(holder: RecyclerView.ViewHolder, id: Int): String {
        return holder.itemView.context.getString(id)
    }

    fun getString(holder: RecyclerView.ViewHolder, id: Int, vararg formatArgs: Any): String {
        return holder.itemView.context.getString(id, *formatArgs)
    }

    fun getString(id: Int, vararg formatArgs: Any): String {
        return itemView.context.getString(id, *formatArgs)
    }

    fun getDrawable(holder: RecyclerView.ViewHolder, id: Int): Drawable? {
        return ContextCompat.getDrawable(holder.itemView.context, id)
    }

    fun getDrawable(id: Int): Drawable? {
        return ContextCompat.getDrawable(this.itemView.context, id)
    }

    fun getColor(holder: RecyclerView.ViewHolder, id: Int): Int {
        return ContextCompat.getColor(holder.itemView.context, id)
    }

    fun getColor(id: Int): Int {
        return ContextCompat.getColor(itemView.context, id)
    }
}
