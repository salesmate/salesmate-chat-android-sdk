package com.rapidops.salesmatechatsdk.app.fragment.chat.adapter

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.rapidops.salesmatechatsdk.R
import com.rapidops.salesmatechatsdk.app.base.BaseRecyclerViewAdapter
import com.rapidops.salesmatechatsdk.app.extension.fromNormalHtml
import com.rapidops.salesmatechatsdk.app.extension.getEmoji
import com.rapidops.salesmatechatsdk.app.extension.getEmojiByUnicode
import com.rapidops.salesmatechatsdk.databinding.REmojiBinding
import com.rapidops.salesmatechatsdk.domain.models.EmojiMapping


internal class EmojiAdapter : BaseRecyclerViewAdapter<EmojiMapping>() {

    private var enableSelection: Boolean = true
    private var selectedRateId: String? = null

    override fun getRowLayoutId(viewType: Int): Int {
        return R.layout.r_emoji
    }

    override fun bind(viewHolder: RecyclerView.ViewHolder, position: Int, item: EmojiMapping) {
        val holder = viewHolder as EmojiViewHolder
        val text = item.unicode.getEmoji()
//        val text = item.unicode.getEmojiByUnicode()

        holder.bind.txtEmoji.text = text.fromNormalHtml()
        if (selectedRateId.isNullOrEmpty()) {
            holder.bind.txtEmoji.alpha = 1f
        } else {
            holder.bind.txtEmoji.alpha = if (item.id == selectedRateId) 1f else 0.5f
        }

        if (enableSelection) {
            holder.bind.txtEmoji.setOnClickListener {
                selectedRateId = item.id
                clickListener?.onItemClick(position, item)
                notifyDataSetChanged()
            }
            holder.bind.txtEmoji.isClickable = true
        } else {
            holder.bind.txtEmoji.setOnClickListener(null)
            holder.bind.txtEmoji.isClickable = false
        }

    }

    override fun getViewHolder(view: View, viewType: Int): RecyclerView.ViewHolder {
        return EmojiViewHolder(view)
    }

    fun setSelectedRateId(selectedRateId: String, enableSelection: Boolean) {
        this.selectedRateId = selectedRateId
        this.enableSelection = enableSelection
    }

    inner class EmojiViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val bind = REmojiBinding.bind(itemView)
    }


}