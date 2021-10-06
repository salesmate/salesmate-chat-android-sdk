package com.rapidops.salesmatechatsdk.app.base

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.rapidops.salesmatechatsdk.app.interfaces.IItemListener
import java.util.*

abstract class BaseRecyclerViewAdapter<ITEM_TYPE> :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    var clickListener: IItemListener<ITEM_TYPE>? = null
    protected lateinit var context: Context
    protected var dataList: MutableList<ITEM_TYPE> = ArrayList()

    abstract fun getRowLayoutId(viewType: Int): Int

    abstract fun bind(viewHolder: RecyclerView.ViewHolder, position: Int, item: ITEM_TYPE)

    abstract fun getViewHolder(view: View, viewType: Int): RecyclerView.ViewHolder

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        context = parent.context
        val view =
            LayoutInflater.from(parent.context).inflate(getRowLayoutId(viewType), parent, false)
        return getViewHolder(view, viewType)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        bind(holder, position, dataList[position])
    }

    override fun getItemCount(): Int {
        return dataList.size
    }

    open fun addItems(items: MutableList<ITEM_TYPE>) {
        if (items.isNotEmpty()) {
            val previousSize = itemCount
            if (dataList.addAll(items)) {
                notifyItemRangeInserted(previousSize, items.size)
            }
        }
    }

    open fun setItems(items: MutableList<ITEM_TYPE>) {
        dataList = items
        notifyDataSetChanged()
    }

    fun removeItem(position: Int) {
        dataList.removeAt(position)
        notifyItemRemoved(position)
    }

    fun clearItems() {
        dataList.clear()
        notifyDataSetChanged()
    }

    fun getItems(): MutableList<ITEM_TYPE> {
        return dataList
    }

}
