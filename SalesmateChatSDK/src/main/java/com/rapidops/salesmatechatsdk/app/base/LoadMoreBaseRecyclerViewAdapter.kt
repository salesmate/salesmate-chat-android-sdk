package com.rapidops.salesmatechatsdk.app.base

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.rapidops.salesmatechatsdk.R
import com.rapidops.salesmatechatsdk.app.interfaces.IItemListener
import java.io.Serializable
import java.util.*

abstract class LoadMoreBaseRecyclerViewAdapter<ITEM_TYPE : Serializable> :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    companion object {
        const val VIEW_TYPE_PROGRESS = 2
    }

    var showLoadMore = false
    var clickListener: IItemListener<ITEM_TYPE>? = null
    protected lateinit var context: Context
    protected var dataList: MutableList<ITEM_TYPE> = ArrayList<ITEM_TYPE>()

    abstract fun getRowLayoutId(viewType: Int): Int

    abstract fun bind(viewHolder: RecyclerView.ViewHolder, position: Int, item: ITEM_TYPE)

    abstract fun getViewHolder(view: View, viewType: Int): RecyclerView.ViewHolder

    /*abstract fun setAccessibility(view: View, itemType: ITEM_TYPE, viewType: Int)*/
    abstract fun getViewType(position: Int): Int

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        context = parent.context
        if (viewType == VIEW_TYPE_PROGRESS && showLoadMore) {
            val view =
                LayoutInflater.from(parent.context).inflate(R.layout.r_progress, parent, false)
            return LoaderViewHolder(view)
        } else {
            val view =
                LayoutInflater.from(parent.context).inflate(getRowLayoutId(viewType), parent, false)
            return getViewHolder(view, viewType)
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (position == itemCount - 1 && showLoadMore) {
            (holder as LoaderViewHolder).itemView.setVisibility(
                if (showLoadMore) View.VISIBLE else View.GONE
            )
            return
        }
        bind(holder, position, dataList[position])
    }

    override fun getItemViewType(position: Int): Int {
        return if (position == itemCount - 1 && showLoadMore) VIEW_TYPE_PROGRESS else getViewType(
            position
        )
    }

    override fun getItemCount(): Int {
        if (dataList.size <= 0) {
            return 0
        }
        if (showLoadMore) {
            return dataList.size + 1
        } else {
            return dataList.size
        }
    }

    open fun add(item: ITEM_TYPE) {
        dataList.add(item)
        notifyDataSetChanged()
    }

    open fun addItems(items: MutableList<ITEM_TYPE>) {
        if (!items.isEmpty()) {
            val previousSize = itemCount
            if (dataList.addAll(items)) {
                notifyItemRangeInserted(previousSize, items.size)
            }
        }
    }

    fun setItems(items: MutableList<ITEM_TYPE>) {
        dataList = items
        notifyDataSetChanged()
    }


    fun clearItems() {
        dataList.clear()
        notifyDataSetChanged()
    }

    fun getItems(): MutableList<ITEM_TYPE> {
        return dataList
    }

    class LoaderViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)

    fun showLoadMore(showLoadMore: Boolean) {
        // Return if already shown/hide
        if (this.showLoadMore == showLoadMore) {
            return
        }
        this.showLoadMore = showLoadMore
        if (showLoadMore) {
            notifyItemInserted(itemCount - 1)
        } else {
            notifyItemRemoved(itemCount)
        }
    }
}
